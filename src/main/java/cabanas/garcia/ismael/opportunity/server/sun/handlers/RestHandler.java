package cabanas.garcia.ismael.opportunity.server.sun.handlers;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Principal;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RestHandler extends AbstractHttpHandler{
    private final Controllers controllers;
    private final UserService userService;
    private final PermissionChecker permissionChecker;

    public RestHandler(Controllers controllers, UserService userService, PermissionChecker permissionChecker) {
        this.controllers = controllers;
        this.userService = userService;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        log.debug("Handle request {}", request);

        Principal principal = request.getPrincipal();
        
        if(principal != null){
            log.debug("User {} authenticated", principal.getName());

            Optional<User> user = userService.findByUsername(principal.getName());
            if(user.isPresent()){
                if(permissionChecker.hasPermission(user.get(), request.getResource())){
                    Controller controller = controllers.select(request);

                    View view = controller.process(request);

                    Response response = view.render();

                    process(httpExchange, response);
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
        else{
            log.debug("No user authenticated");
            HttpExchangeUtil.unauthorized(httpExchange);
        }
    }


    private void process(HttpExchange httpExchange, Response response) throws IOException {
        HttpExchangeUtil.write(httpExchange, response.getStatusCode(), "text/html", response.getContent());
    }
}
