package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
import cabanas.garcia.ismael.opportunity.support.Resource;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class ExchangeRequest implements Request {

    private static final String REDIRECCT_PARAM = "redirecct";
    private final Resource resource;

    private HttpExchange httpExchange;
    private Map<String, String> parameters;
    private Session session;
    private final RequestMethodEnum method;

    public ExchangeRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.parameters = new HashMap<>();
        this.resource = Resource.builder().path(httpExchange.getRequestURI().getPath()).build();
        this.method = RequestMethodEnum.valueOf(httpExchange.getRequestMethod());
    }

    private void readParametersRequest() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
            String urlencoded = reader.readLine();
            if (urlencoded != null && urlencoded.trim().length() > 0) {
                List<NameValuePair> list = URLEncodedUtils.parse(urlencoded, Charset.defaultCharset());
                list.forEach(pair -> {
                    parameters.put(pair.getName(), pair.getValue());
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Error dealing with request body", e);
        }
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public Optional<Session> getSession() {
        return Optional.ofNullable((Session) httpExchange.getAttribute("session"));
    }

    @Override
    public RequestMethodEnum getMethod() {
        return this.method;
    }

    @Override
    public String getParameter(String paramName) {
        if(parameters.isEmpty()){
            readParametersRequest();
        }
        return parameters.get(paramName);
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
        httpExchange.setAttribute("session", session);
    }

    @Override
    public boolean hasRedirectParameter() {
        return getParameter(REDIRECCT_PARAM) != null;
    }

    @Override
    public Optional<Cookie> getSessionCookie() {
        log.debug("Looking for session cookie");
        Headers headers = httpExchange.getRequestHeaders();
        if(!headers.isEmpty()) {
            List<String> headerCookies = headers.get(RequestHeadersEnum.COOKIE.getName());
            if(headerCookies != null) {
                Cookies cookies = CookieAdapter.toCookies(headerCookies);
                Optional<Cookie> cookie = cookies.get(Cookie.SESSION_TOKEN);
                if(cookie.isPresent())
                    log.debug("Cookie founded {}", cookie.get());
                else
                    log.debug("Cookie {} not found", Cookie.SESSION_TOKEN);
                return cookie;
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "ExchangeRequest{" +
                "resource=" + resource +
                ", method=" + method +
                '}';
    }
}