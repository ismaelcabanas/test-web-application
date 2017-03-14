package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SunHttpAuthorizationFilter extends Filter{

    public static final String AUTHENTICATION_FILTER = "Authentication filter";
    private PrivateResources privateResources;

    private SessionRepository sessionRepository;

    private AuthorizationFilterConfiguration configuration;

    public SunHttpAuthorizationFilter(AuthorizationFilterConfiguration configuration) {
        this.configuration = configuration;

        this.privateResources = new PrivateResources();
        this.configuration.getPrivateResources().stream().forEach(resource -> this.privateResources.add(resource));
    }

    public SunHttpAuthorizationFilter(AuthorizationFilterConfiguration configuration, SessionRepository sessionRepository) {
        this(configuration);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);

        Resource resource = extractorHttpExchange.extractPathFrom();

        log.debug("Filtering resource {}", resource);

        if(this.privateResources.hasResource(resource)){
            log.debug("is a secure resource...");
            Optional<Cookie> sessionCookie = extractorHttpExchange.extractSessionCookie();
            if(!sessionCookie.isPresent()){
                log.info("Not exist a valid cookie session, redirecting for authentication to {}", configuration.getRedirectPath());
                HttpExchangeUtil.redirect(httpExchange, configuration.getRedirectPath());
                return;
            }
            Optional<Session> session = getSession(sessionCookie.get());
            if(session.isPresent()){
                Session theSession = session.get();
                if(!theSession.hasExpired()) {
                    log.debug("Session non expired");
                    theSession.resetLastAccess();
                    sessionRepository.persist(theSession);
                    log.debug("Updated session {}", theSession);
                    httpExchange.setAttribute("session", theSession);
                }
                else{
                    log.debug("Session expired. Deleting session {}", theSession.getSessionId());
                    sessionRepository.delete(theSession.getSessionId());
                    log.info("Session expired, redirecting for authentication to {}", configuration.getRedirectPath());
                    HttpExchangeUtil.redirect(httpExchange, configuration.getRedirectPath());
                    return;
                }
            }else{
                log.info("Non exist user session. Redirect to {} ", configuration.getRedirectPath());
                HttpExchangeUtil.redirect(httpExchange, configuration.getRedirectPath());
                return;
            }
        }
        chain.doFilter(httpExchange);
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

    @Override
    public String description() {
        return AUTHENTICATION_FILTER;
    }

    public AuthorizationFilterConfiguration getConfiguration() {
        return configuration;
    }

    public static class AuthorizationFilterConfiguration {
        private String redirectPath;
        private List<Resource> privateResources = new ArrayList<>();

        public void redirectPath(String redirectPath) {
            this.redirectPath = redirectPath;
        }

        public void addPrivateResource(String privateResource) {
            this.privateResources.add(Resource.builder().path(privateResource).build());
        }


        public String getRedirectPath() {
            return redirectPath;
        }

        public List<Resource> getPrivateResources() {
            return privateResources;
        }
    }

}
