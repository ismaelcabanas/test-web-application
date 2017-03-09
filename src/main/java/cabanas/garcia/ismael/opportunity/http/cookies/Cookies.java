package cabanas.garcia.ismael.opportunity.http.cookies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cookies {
    private List<Cookie> cookies = new ArrayList<>();

    public void add(final Cookie cookie) {
        cookies.add(cookie);
    }

    public Optional<Cookie> get(String cookieName) {
        Optional<Cookie> myCookie = cookies.stream().filter(
                cookie -> cookie.getName().equals(cookieName)
        ).findFirst();

        return myCookie;
    }
}
