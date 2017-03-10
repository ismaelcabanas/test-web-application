package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;


public class HttpExchangeWithCredentials extends HttpExchangeStub {
    private static final String payload = "username=ismael&" +
            "password=failed";

    public HttpExchangeWithCredentials() {
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
