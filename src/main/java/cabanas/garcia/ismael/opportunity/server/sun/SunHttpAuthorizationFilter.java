package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionValidator;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.service.PrivateResourcesService;
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

    private PrivateResourcesService privateResourcesService;
    private SessionValidator sessionValidator;
    private PermissionChecker permissionChecker;
    private AuthorizationFilterConfiguration configuration;

    public SunHttpAuthorizationFilter(AuthorizationFilterConfiguration configuration) {
        // TODO Delete this constructor
        this.configuration = configuration;
    }

    public SunHttpAuthorizationFilter(AuthorizationFilterConfiguration configuration, SessionValidator sessionValidator, PermissionChecker permissionChecker, PrivateResourcesService privateResourcesService) {
        this(configuration);
        this.sessionValidator = sessionValidator;
        this.permissionChecker = permissionChecker;
        this.privateResourcesService = privateResourcesService;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        Resource resource = request.getResource();

        log.debug("Filtering resource {}", resource);

        if(privateResourcesService.hasResource(resource)){
            log.debug("{} is a secure resource...", resource.getPath());
            Optional<Session> session = sessionValidator.validate(request);
            if(session.isPresent()){
                if(permissionChecker.hasPermission(session.get().getUser(), resource)){
                    log.debug("User {} has permission to resource {}", session.get().getUser().getUsername(), resource);
                    chain.doFilter(httpExchange);
                }
                else{
                    log.debug("User {} hasn't permission to resource {} and will be redirected to {}", session.get().getUser().getUsername(), resource, configuration.getRedirectForbiddenPath());
                    HttpExchangeUtil.redirect(httpExchange, configuration.getRedirectForbiddenPath());
                }
            }
            else{
                log.info("Non exist user session. Redirecting to authenticate to {} ", configuration.getRedirectPath());
                HttpExchangeUtil.redirect(httpExchange, configuration.getRedirectPath());
            }
        }
        else {
            chain.doFilter(httpExchange);
        }
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
        private String redicectForbiddenPath;

        public void redirectPath(String redirectPath) {
            this.redirectPath = redirectPath;
        }
        public void redirectForbiddenPath(String redicectForbiddenPath) {
            this.redicectForbiddenPath = redicectForbiddenPath;
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

        public String getRedirectForbiddenPath() {
            return redicectForbiddenPath;
        }
    }

}
