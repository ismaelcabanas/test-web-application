package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import com.sun.net.httpserver.HttpPrincipal;

public class HttpExchangeRestWithoutPrincipalResourceStub extends HttpExchangeRestPostWithPrincipalResourceStub {
    public HttpExchangeRestWithoutPrincipalResourceStub(String path) {
        super(path, "");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
}
