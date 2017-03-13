package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;

public class HttpExchangeWithSessionStub extends HttpExchangeSuccessResourceStub {
    private Session session;

    public HttpExchangeWithSessionStub(String path, String aSessionId) {
        super(path);
        this.session = Session.builder().sessionId(aSessionId).build();
    }

    public HttpExchangeWithSessionStub(String path) {
        super(path);
    }

    @Override
    public Object getAttribute(String name) {
        return session;
    }
}
