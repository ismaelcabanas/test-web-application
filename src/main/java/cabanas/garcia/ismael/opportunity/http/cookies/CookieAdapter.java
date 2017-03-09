package cabanas.garcia.ismael.opportunity.http.cookies;

import java.util.List;
import java.util.Optional;

public final class CookieAdapter {
    private CookieAdapter(){}

    public static Cookies toCookies(List<String> cookiesList) {
        Cookies cookies = new Cookies();

        cookiesList.forEach(rawCookie -> {
            Optional<Cookie> cookie = toCookie(rawCookie);
            if(cookie.isPresent())
                cookies.add(cookie.get());
        });
        return cookies;
    }

    private static Optional<Cookie> toCookie(String rawCookie) {
        String[] rawCookieSplitted = rawCookie.split("=");
        if(rawCookieSplitted.length == 2){
            return Optional.of(Cookie.builder()
                                .name(rawCookieSplitted[0])
                                .value(rawCookieSplitted[1])
                                .build());
        }
        return Optional.empty();
    }
}
