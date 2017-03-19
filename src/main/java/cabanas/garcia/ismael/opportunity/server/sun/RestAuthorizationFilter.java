package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Principal;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RestAuthorizationFilter extends Filter{

    private static final String REST_AUTHORIZATION_FILTER_DESCRIPTION = "REST_AUTHORIZATION_FILTER";
    private final UserService userService;
    private final PermissionChecker permissionChecker;

    public RestAuthorizationFilter(UserService userService, PermissionChecker permissionChecker) {
        this.userService = userService;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        log.debug("Handle request {}", request);

        Principal principal = request.getPrincipal();

        if(principal != null){
            log.debug("User {} authenticated", principal.getName());

            if(!request.getMethod().equals(RequestMethodEnum.GET)) {
                Optional<User> user = userService.findByUsername(principal.getName());
                if(user.isPresent()){
                    if(permissionChecker.hasPermission(user.get(), request.getResource())){
                        chain.doFilter(httpExchange);
                    }
                    else{
                        HttpExchangeUtil.forbidden(httpExchange);
                    }
                }
                else{
                    log.debug("There is principal but no user found, then unauthorizated user");
                    HttpExchangeUtil.unauthorized(httpExchange);
                }
            }
            else {
                chain.doFilter(httpExchange);
            }
        }
        else{
            log.debug("No user authenticated");
            HttpExchangeUtil.unauthorized(httpExchange);
        }
    }

    @Override
    public String description() {
        return REST_AUTHORIZATION_FILTER_DESCRIPTION;
    }
}
