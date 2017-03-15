package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;

import java.util.Optional;

public interface SessionManager {
    Optional<Session> validate(Request request);

    Session update(Session session);
}
