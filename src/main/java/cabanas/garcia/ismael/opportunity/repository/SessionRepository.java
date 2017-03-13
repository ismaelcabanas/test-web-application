package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.http.Session;

public interface SessionRepository {
    Session findBy(String sessionId);
}
