package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;

public class HttpExchangeRestPostWithPrincipalResourceStub extends HttpExchangeRestGetWithPrincipalResourceStub {
    public HttpExchangeRestPostWithPrincipalResourceStub(String path, String username) {
        super(path, username);
    }

    @Override
    public String getRequestMethod() {
        return RequestMethodEnum.POST.getName();
    }
}
