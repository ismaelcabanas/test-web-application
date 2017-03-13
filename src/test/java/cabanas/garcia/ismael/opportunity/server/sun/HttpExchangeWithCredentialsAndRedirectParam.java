package cabanas.garcia.ismael.opportunity.server.sun;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpExchangeWithCredentialsAndRedirectParam extends HttpExchangeWithCredentialsStub {

    private final String username;
    private final String password;
    private final String redirectPath;

    public HttpExchangeWithCredentialsAndRedirectParam(String username, String password, String redirectPath) {
        this.username = username;
        this.password = password;
        this.redirectPath = redirectPath;
    }

    @Override
    public InputStream getRequestBody() {
        String payload = "username=" + username + "&" +
                "password=" + password + "&redirect=" + redirectPath;
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }
}
