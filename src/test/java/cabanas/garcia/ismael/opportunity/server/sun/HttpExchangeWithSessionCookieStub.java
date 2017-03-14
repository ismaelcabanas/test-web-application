package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.RequestHeadersConstants;

public class HttpExchangeWithSessionCookieStub extends HttpExchangeSuccessResourceStub {
    private final String sessionId;

    public HttpExchangeWithSessionCookieStub(String path, String aSessionId) {
        super(path);
        this.sessionId = aSessionId;
        getRequestHeaders().add(RequestHeadersConstants.COOKIE.getName(), Cookie.builder().name("sessionToken").value(sessionId).build().toString());
    }

}
