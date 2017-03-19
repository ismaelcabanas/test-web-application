package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

public class HttpExchangeWithDataForApiStub extends HttpExchangeStub {

    private String username;
    private String password;
    private String roles;

    public HttpExchangeWithDataForApiStub(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        getRequestHeaders().add("Content-type", "application/x-www-form-urlencoded");
    }

    public HttpExchangeWithDataForApiStub(String username, String roles) {
        this(username, "", roles);
    }

    @Override
    public InputStream getRequestBody() {
        String payload ="username=" + username + "&password=" + password + "&roles=" + roles;
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }

    @Override
    public URI getRequestURI() {
        return URI.create("/users");
    }
}
