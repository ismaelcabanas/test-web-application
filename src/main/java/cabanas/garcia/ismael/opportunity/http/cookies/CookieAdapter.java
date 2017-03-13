package cabanas.garcia.ismael.opportunity.http.cookies;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public final class CookieAdapter {
    private CookieAdapter(){}

    public static Cookies toCookies(List<String> cookiesList) {
        Cookies cookies = new Cookies();

        cookiesList.forEach(rawCookie -> {
            StringTokenizer rawCookieTokenized = new StringTokenizer(rawCookie, ";");
            while(rawCookieTokenized.hasMoreElements()){
                Optional<Cookie> cookie = toCookie(rawCookieTokenized.nextToken());
                if(cookie.isPresent() && cookie.get().hasValue())
                    cookies.add(cookie.get());
            }
        });
        return cookies;
    }

    private static Optional<Cookie> toCookie(String rawCookie) {
        String[] rawCookieSplitted = rawCookie.split("=");
        if(rawCookieSplitted.length == 2){
            return Optional.of(Cookie.builder()
                                .name(rawCookieSplitted[0].trim())
                                .value(rawCookieSplitted[1].trim())
                                .build());
        }
        return Optional.empty();
    }
}
