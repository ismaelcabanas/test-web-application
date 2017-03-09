package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.cookies.CookieAdapter;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookies;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

        String qry = "";
        InputStream in = httpExchange.getRequestBody();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }
            qry = new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // parse the request body
        Map<String,List<String>> parms = new HashMap<>();
        String defs[] = qry.split("[&]");
        for (String def: defs) {
            int ix = def.indexOf('=');
            String name;
            String value;
            Parameter parameter = null;
            if (ix < 0) {
                parameter = Parameter.builder().name(def).value("").build();
            } else {
                parameter = Parameter.builder().name(def.substring(0, ix)).value(def.substring(ix+1)).build();
            }
            parameters.add(parameter);
        }
        return parameters;
    }

    public String getMethod() {
        return httpExchange.getRequestMethod();
    }
}
