package cabanas.garcia.ismael.opportunity.server.sun;

public class HttpExchangeAuthenticatedUserStub extends HttpExchangeWithSessionCookieStub {
    private static final String SESSION_ID = "aSessionId";
    public HttpExchangeAuthenticatedUserStub(String path) {
        super(path, SESSION_ID);
    }
}
