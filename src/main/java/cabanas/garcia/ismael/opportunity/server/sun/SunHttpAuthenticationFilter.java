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

        log.info("Filtering resource {}", path);

        if(isPrivateResource(path)){
            log.info("Looking for valid user session...");
            Optional<Cookie> sessionCookie = extractorHttpExchange.extractSessionCookie();
            if(!sessionCookie.isPresent()){
                log.info("Not exist a valid user session, redirecting for authentication to " + configuration.getRedirectPath());
                httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, configuration.getRedirectPath());
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
                return;
            }
            Session session = getSession(sessionCookie.get());
            httpExchange.setAttribute("session", session);
        }
        chain.doFilter(httpExchange);
    }

    private Session getSession(final Cookie sessionCookie) {
        return sessionRepository.findBy(sessionCookie.getValue());
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
