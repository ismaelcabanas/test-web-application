package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;

public class HttpExchangeRestPutWithPrincipalResourceStub extends HttpExchangeRestGetWithPrincipalResourceStub {
    public HttpExchangeRestPutWithPrincipalResourceStub(String path, String username) {
        super(path, username);
    }

    @Override
    public String getRequestMethod() {
        return RequestMethodEnum.PUT.getName();
    }
}
