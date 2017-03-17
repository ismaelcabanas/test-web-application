package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.util.UUIDProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDRandomProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DefaultSessionManager implements SessionManager {
    private final SessionRepository sessionRepository;
    private final UUIDProvider uuidProvider;

    public DefaultSessionManager(SessionRepository sessionRepository) {
        this(sessionRepository, new UUIDRandomProvider());
    }

    public DefaultSessionManager(SessionRepository sessionRepository, UUIDProvider uuidProvider) {
        this.sessionRepository = sessionRepository;
        this.uuidProvider = uuidProvider;
    }

    @Override
    public Optional<Session> validate(Request request) {

        Optional<Cookie> cookieSessionToken = request.getSessionCookie();

        if(cookieSessionToken.isPresent()) {
            String sessionId = cookieSessionToken.get().getValue();
            Optional<Session> session = sessionRepository.read(sessionId);
            if(session.isPresent()){
                Session theSession = session.get();
                if(!theSession.hasExpired()){
                    return session;
                }
                else{
                    log.debug("Session with sessionId {} expired", sessionId);
                    sessionRepository.delete(sessionId);
                }
            }
            else{
                log.debug("Session with sessionId {} not founded.", sessionId);
            }
        }

        log.debug("Not exist a valid cookie session");
        return Optional.empty();
    }

    @Override
    public Session update(Session session) {
        session.resetLastAccess();
        return sessionRepository.persist(session);
    }

    @Override
    public void invalidate(Session session) {
        sessionRepository.delete(session.getSessionId());
        session.invalidate();
    }

    @Override
    public Session create(User user, int sessionTimeout) {
        Session session = Session.create(user, sessionTimeout, uuidProvider);

        sessionRepository.persist(session);

        log.debug("Session created {}", session);

        return session;
    }
}
