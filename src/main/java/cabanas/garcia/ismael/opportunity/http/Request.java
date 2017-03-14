package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.support.Resource;

import java.util.Optional;

public interface Request {
    Resource getPath();

    Optional<Session> getSession();

    RequestMethodEnum getMethod();

    String getParameter(String paramName);

    void setSession(Session session);

    boolean hasRedirectParameter();
}
