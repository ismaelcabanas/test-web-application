package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import cabanas.garcia.ismael.opportunity.http.Session;

public class HttpExchangeWithSessionStub extends HttpExchangeSuccessResourceStub {
    private Session session;

    public HttpExchangeWithSessionStub(String path, String aSessionId) {
        super(path);
        this.session = Session.builder().sessionId(aSessionId).build();
        setAttribute("session", session);
    }

    public HttpExchangeWithSessionStub(String path) {
        super(path);
    }
    
}
