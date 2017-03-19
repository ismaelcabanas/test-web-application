package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

public class HttpExchangeWithPathVariableStub extends HttpExchangeStub {
    private final String pathVariable;
    private final String path;
    private String roles;

    public HttpExchangeWithPathVariableStub(String path, String pathVariable) {
        this.path = path;
        this.pathVariable = pathVariable;
    }

    public HttpExchangeWithPathVariableStub(String path, String pathVariable, String roles) {
        this(path, pathVariable);
        this.roles = roles;
        getRequestHeaders().add("Content-type", "application/x-www-form-urlencoded");
    }

    @Override
    public InputStream getRequestBody() {
        if(roles != null) {
            String payload = "roles=" + roles;
            InputStream is = new ByteArrayInputStream(payload.getBytes());
            return is;
        }
        return super.getRequestBody();
    }

    @Override
    public URI getRequestURI() {
        return URI.create(path + "/" + pathVariable);
    }
}
