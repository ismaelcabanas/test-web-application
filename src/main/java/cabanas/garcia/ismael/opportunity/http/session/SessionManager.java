package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.model.User;

import java.util.Optional;

public interface SessionManager {
    Optional<Session> validate(Request request);

    Session update(Session session);

    void invalidate(Session session);

    Session create(User user, int sessionTimeout);
}
