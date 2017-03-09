package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Optional;

public class ExtractorHttpExchange {
    private final HttpExchange httpExchange;

    public ExtractorHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public String extractPathFrom() {
        return httpExchange.getRequestURI().getPath();
    }

    public Optional<Session> extractSessionFrom() {
        Headers headers = httpExchange.getRequestHeaders();
        if(!headers.isEmpty()) {
            List<String> headerCookies = headers.get(RequestHeadersConstants.COOKIE);
            Cookies cookies = CookieAdapter.toCookies(headerCookies);
            Optional<Cookie> cookie = cookies.get(Cookie.SESSION_TOKEN);
            if(cookie.isPresent()){
                return Optional.of(Session.builder().sessionId(cookie.get().getValue()).build());
            }
        }
        return Optional.empty();
    }
}
