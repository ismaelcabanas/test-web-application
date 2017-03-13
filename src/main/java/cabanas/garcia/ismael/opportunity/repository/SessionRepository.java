package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.http.Session;

import java.util.Optional;

public interface SessionRepository {
    Optional<Session> read(String sessionId);

    Session persist(Session session);

    void deleteAll();

    void delete(String sessionId);
}
