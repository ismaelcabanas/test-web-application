package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
import cabanas.garcia.ismael.opportunity.support.Resource;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class ExtractorHttpExchange {
    private final HttpExchange httpExchange;

    public ExtractorHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public Resource extractPathFrom() {
        return Resource.builder().path(httpExchange.getRequestURI().getPath()).build();
    }

    public Optional<Cookie> extractSessionCookie() {
        log.debug("Looking for session cookie");
        Headers headers = httpExchange.getRequestHeaders();
        if(!headers.isEmpty()) {
            List<String> headerCookies = headers.get(RequestHeadersEnum.COOKIE.getName());
            if(headerCookies != null) {
                Cookies cookies = CookieAdapter.toCookies(headerCookies);
                Optional<Cookie> cookie = cookies.get(Cookie.SESSION_TOKEN);
                log.debug("Cookie founded {}", cookie.get());
                return cookie;
            }
        }
        return Optional.empty();
    }

    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
            String urlencoded = reader.readLine();
            if(urlencoded != null && urlencoded.trim().length() > 0) {
                List<NameValuePair> list = URLEncodedUtils.parse(urlencoded, Charset.defaultCharset());
                list.forEach(pair -> {
                    parameters.add(Parameter.builder().name(pair.getName()).value(pair.getValue()).build());
                });
            }
        } catch(IOException e) {
            log.error("Error dealing with request body", e);
        }

        return parameters;
    }

    public RequestMethodEnum getMethod() {
        return RequestMethodEnum.valueOf(httpExchange.getRequestMethod());
    }

    public Optional<Session> extractSession() {
        return Optional.ofNullable((Session)httpExchange.getAttribute("session"));
    }
}
