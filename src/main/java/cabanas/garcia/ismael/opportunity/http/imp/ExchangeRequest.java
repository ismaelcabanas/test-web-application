package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class ExchangeRequest implements Request {

    private final Resource resource;

    private HttpExchange httpExchange;
    private Map<String, String> requestBodyParameters;
    private final RequestMethodEnum method;
    private Map<String, String> queryParameters;

    public ExchangeRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.requestBodyParameters = new HashMap<>();
        this.method = RequestMethodEnum.valueOf(httpExchange.getRequestMethod());
        this.resource = Resource.builder().path(httpExchange.getRequestURI().getPath()).method(this.method).build();
        this.queryParameters = HttpExchangeUtil.parseQueryParameters(httpExchange);
    }

    private void readParametersRequest() {
        Map<String, String> parameters = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
            String urlencoded = reader.readLine();
            if (urlencoded != null && urlencoded.trim().length() > 0) {
                List<NameValuePair> list = URLEncodedUtils.parse(urlencoded, Charset.defaultCharset());
                list.forEach(pair -> parameters.put(pair.getName(), pair.getValue()));
            }
        } catch (IOException e) {
            log.error("Error dealing with request body", e);
        }
        this.requestBodyParameters = Collections.unmodifiableMap(parameters);
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
        if(requestBodyParameters.isEmpty()){
            readParametersRequest();
        }
        return requestBodyParameters.get(paramName);
    }

    @Override
    public void setSession(Session session) {
        httpExchange.setAttribute("session", session);
    }

    @Override
    public boolean hasRedirectParameter() {
        return StringUtils.isNoneEmpty(getParameter(RequestConstants.REDIRECCT_PARAM));
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
    public String getQueryParameter(String paramName) {
        return queryParameters.get(paramName);
    }

    @Override
    public Principal getPrincipal() {
        HttpPrincipal httpPrincipal = httpExchange.getPrincipal();
        if(httpPrincipal != null) {
            return DefaultPrincipal.builder().name(httpPrincipal.getName()).build();
        }
        return null;
    }

    @Override
    public String toString() {
        return "ExchangeRequest{" +
                "resource=" + resource +
                ", method=" + method +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ExchangeRequest that = (ExchangeRequest) o;

        if (!resource.equals(that.resource)){
            return false;
        }
        return method == that.method;
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
