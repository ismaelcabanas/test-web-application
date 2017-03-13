package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.http.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class InMemorySessionRepository implements SessionRepository{
    private static InMemorySessionRepository instance = null;

    private Map<String, Session> repository = new HashMap<>();

    private InMemorySessionRepository() {
    }

    public static InMemorySessionRepository getInstance() {
        if(instance == null){
            instance = new InMemorySessionRepository();
        }
        return instance;
    }

    @Override
    public Optional<Session> read(String sessionId) {
        log.debug("Getting session {} from repository", sessionId);
        return Optional.ofNullable(repository.get(sessionId));
    }

    @Override
    public Session persist(Session newSession) {
        repository.put(newSession.getSessionId(), newSession);
        log.debug("Persisted session {} in repository", newSession);
        return newSession;
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public void delete(String sessionId) {
        log.debug("Deleting session {} from repository", sessionId);
        repository.remove(sessionId);
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }
}
