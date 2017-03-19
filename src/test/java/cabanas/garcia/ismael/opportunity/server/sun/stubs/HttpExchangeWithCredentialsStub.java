package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;


public class HttpExchangeWithCredentialsStub extends HttpExchangeStub {
    private static final String payload = "username=ismael&" +
            "password=failed";

    public HttpExchangeWithCredentialsStub() {
        super();
        getRequestHeaders().add("Content-type", "application/x-www-form-urlencoded");
    }

    @Override
    public InputStream getRequestBody() {
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }

    @Override
    public URI getRequestURI() {
        return URI.create("/test");
    }
}
