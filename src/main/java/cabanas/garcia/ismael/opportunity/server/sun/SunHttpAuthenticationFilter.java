package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SunHttpAuthenticationFilter extends Filter{

    public static final String AUTHENTICATION_FILTER = "Authentication filter";

    private SessionRepository sessionRepository;

    private final AuthenticatorFilterConfiguration configuration;

    public SunHttpAuthenticationFilter() {
        this.configuration = new AuthenticatorFilterConfiguration();
    }

    public SunHttpAuthenticationFilter(SessionRepository sessionRepository) {
        this();
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);

        String path = extractorHttpExchange.extractPathFrom();

        log.debug("Filtering resource {}", path);

        if(isPrivateResource(path)){
            log.debug("is a secure resource...");
            Optional<Cookie> sessionCookie = extractorHttpExchange.extractSessionCookie();
            if(!sessionCookie.isPresent()){
                log.info("Not exist a valid user session, redirecting for authentication to {}", configuration.getRedirectPath());
                redirect(httpExchange);
                return;
            }
            Optional<Session> session = getSession(sessionCookie.get());
            if(session.isPresent()){
                Session theSession = session.get();
                if(!theSession.hasExpired()) {
                    theSession.resetLastAccess();
                    sessionRepository.persist(theSession);
                    log.debug("Updated session {}", theSession);
                    httpExchange.setAttribute("session", theSession);
                }
                else{
                    log.debug("Deleting session {}", theSession.getSessionId());
                    sessionRepository.delete(theSession.getSessionId());
                    log.info("Session expired, redirecting for authentication to {}", configuration.getRedirectPath());
                    redirect(httpExchange);
                    return;
                }
            }
        }
        chain.doFilter(httpExchange);
    }

    private void redirect(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, configuration.getRedirectPath());
        httpExchange.getResponseHeaders().remove(ResponseHeaderConstants.SET_COOKIE);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }

    private Optional<Session> getSession(final Cookie sessionCookie) {
        log.debug("Load session {} from repository", sessionCookie.getValue());
        Optional<Session> sessionFromRepository = sessionRepository.read(sessionCookie.getValue());
        if(sessionFromRepository.isPresent()){
            Session session = sessionFromRepository.get().makeClone();
            log.debug("Session {} loaded", session);
            return Optional.of(session);
        }
        return Optional.empty();
    }

    private boolean isPrivateResource(String resource) {
        return configuration.getPrivateResources().has(resource);
    }

    @Override
    public String description() {
        return AUTHENTICATION_FILTER;
    }

    public AuthenticatorFilterConfiguration getConfiguration() {
        return configuration;
    }

    public static class AuthenticatorFilterConfiguration {
        private String redirectPath;
        private PrivateResources privateResources = new PrivateResources();

        public void redirectPath(String redirectPath) {
            this.redirectPath = redirectPath;
        }

        public void addPrivateResource(String privateResource) {
            this.privateResources.add(privateResource);
        }


        public String getRedirectPath() {
            return redirectPath;
        }

        public PrivateResources getPrivateResources() {
            return privateResources;
        }
    }

    private static class PrivateResources {
        private List<String> resources = new ArrayList<>();

        public void add(String privateResource) {
            this.resources.add(privateResource);
        }

        public boolean has(String resource) {
            return resources.contains(resource);
        }
    }
}
