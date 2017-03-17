package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;

public class HttpExchangeRestDeleteWithPrincipalResourceStub extends HttpExchangeRestGetWithPrincipalResourceStub {
    public HttpExchangeRestDeleteWithPrincipalResourceStub(String path, String username) {
        super(path, username);
    }

    @Override
    public String getRequestMethod() {
        return RequestMethodEnum.DELETE.getName();
    }
}
