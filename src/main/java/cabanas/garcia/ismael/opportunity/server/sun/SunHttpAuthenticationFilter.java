package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.*;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SunHttpAuthenticationFilter extends Filter{

    private final AuthenticatorFilterConfiguration configuration;

    public SunHttpAuthenticationFilter() {
        this.configuration = new AuthenticatorFilterConfiguration();
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);

        String path = extractorHttpExchange.extractPathFrom();

        if(isPrivateResource(path)){
            Optional<Session> session = extractorHttpExchange.extractSessionFrom();
            if(!session.isPresent()){
                httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, configuration.getRedirectPath());
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
                return;
            }
        }
        chain.doFilter(httpExchange);
    }

    private boolean isPrivateResource(String resource) {
        return configuration.getPrivateResources().has(resource);
    }

    @Override
    public String description() {
        return "Authentication filter";
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
