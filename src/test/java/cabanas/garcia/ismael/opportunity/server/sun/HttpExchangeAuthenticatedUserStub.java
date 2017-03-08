package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.HttpExchange;

public class HttpExchangeAuthenticatedUserStub extends HttpExchangeUnauthenticatedUserStub {
    public HttpExchangeAuthenticatedUserStub(String path) {
        super(path);
    }
}
