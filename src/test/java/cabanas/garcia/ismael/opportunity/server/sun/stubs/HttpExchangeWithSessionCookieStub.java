package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.RequestHeadersEnum;

public class HttpExchangeWithSessionCookieStub extends HttpExchangeSuccessResourceStub {
    private final String sessionId;

    public HttpExchangeWithSessionCookieStub(String path, String aSessionId) {
        super(path);
        this.sessionId = aSessionId;
        getRequestHeaders().add(RequestHeadersEnum.COOKIE.getName(), Cookie.builder().name("sessionToken").value(sessionId).build().toString());
    }

}
