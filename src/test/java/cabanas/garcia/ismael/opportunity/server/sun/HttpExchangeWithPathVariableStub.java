package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;

public class HttpExchangeWithPathVariableStub extends HttpExchangeStub {
    private final String pathVariable;
    private final String path;

    public HttpExchangeWithPathVariableStub(String path, String pathVariable) {
        this.path = path;
        this.pathVariable = pathVariable;
    }

    @Override
    public URI getRequestURI() {
        return URI.create(path + "/" + pathVariable);
    }
}
