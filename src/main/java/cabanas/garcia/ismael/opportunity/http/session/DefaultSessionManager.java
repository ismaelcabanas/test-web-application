package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DefaultSessionManager implements SessionManager {
    private final SessionRepository sessionRepository;

    public DefaultSessionManager(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
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
                    log.debug("Session expired. Deleting session {}", sessionId);
                    sessionRepository.delete(sessionId);
                }
            }
            else{
                log.info("Session with sessionId {} not founded.", sessionId);
            }
        }

        log.info("Not exist a valid cookie session");
        return Optional.empty();
    }
}
