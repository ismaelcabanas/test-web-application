package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
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

    public String extractPathFrom() {
        return httpExchange.getRequestURI().getPath();
    }

    public Optional<Session> extractSessionCookie() {
        Headers headers = httpExchange.getRequestHeaders();
        if(!headers.isEmpty()) {
            List<String> headerCookies = headers.get(RequestHeadersConstants.COOKIE);
            if(headerCookies != null) {
                Cookies cookies = CookieAdapter.toCookies(headerCookies);
                Optional<Cookie> cookie = cookies.get(Cookie.SESSION_TOKEN);
                if (cookie.isPresent()) {
                    return Optional.of(Session.builder().sessionId(cookie.get().getValue()).build());
                }
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

    public String getMethod() {
        return httpExchange.getRequestMethod();
    }
}
