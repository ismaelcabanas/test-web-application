package cabanas.garcia.ismael.opportunity.http.cookies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cookies {
    private List<Cookie> cookieList = new ArrayList<>();

    public void add(final Cookie cookie) {
        cookieList.add(cookie);
    }

    public Optional<Cookie> get(String cookieName) {
        return cookieList.stream().filter(
                cookie -> cookie.getName().equals(cookieName)
        ).findFirst();
    }
}
