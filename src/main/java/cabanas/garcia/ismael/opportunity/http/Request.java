package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.support.Resource;

import java.util.Optional;

public interface Request {
    Resource getResource();

    Optional<Session> getSession();

    RequestMethodEnum getMethod();

    String getParameter(String paramName);

    void setSession(Session session);

    boolean hasRedirectParameter();

    Optional<Cookie> getSessionCookie();
}
