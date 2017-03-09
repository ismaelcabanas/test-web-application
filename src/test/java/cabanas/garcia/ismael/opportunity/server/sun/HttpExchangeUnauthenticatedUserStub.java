package cabanas.garcia.ismael.opportunity.server.sun;

import java.net.URI;

public class HttpExchangeUnauthenticatedUserStub extends HttpExchangeStub {
    private final String path;

    public HttpExchangeUnauthenticatedUserStub(String path) {
        this.path = path;
    }

    @Override
    public URI getRequestURI() {
        return URI.create(path);
    }


}
