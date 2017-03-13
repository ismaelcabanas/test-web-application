package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.http.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return Optional.ofNullable(repository.get(sessionId));
    }

    @Override
    public Session persist(Session newSession) {
        repository.put(newSession.getSessionId(), newSession);
        return newSession;
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }
}
