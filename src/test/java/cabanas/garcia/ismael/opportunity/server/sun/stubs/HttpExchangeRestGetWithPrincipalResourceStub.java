package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import com.sun.net.httpserver.HttpPrincipal;

public class HttpExchangeRestGetWithPrincipalResourceStub extends HttpExchangeSuccessResourceStub {

    private final HttpPrincipal principal;

    public HttpExchangeRestGetWithPrincipalResourceStub(String path, String username) {
        super(path);
        principal = new HttpPrincipal(username, "testing_realm");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        return principal;
    }
}
