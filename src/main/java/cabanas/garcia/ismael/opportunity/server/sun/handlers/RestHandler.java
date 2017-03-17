package cabanas.garcia.ismael.opportunity.server.sun.handlers;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Principal;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.server.sun.AbstractHttpHandler;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
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

        if(!authenticated(request)){
            log.debug("There is principal but no user found, then unauthorizated user");
            HttpExchangeUtil.unauthorized(httpExchange);
            return;
        }

        Optional<User> user = userService.findByUsername(principal.getName());

        if(!authorized(user, request.getResource())){
            log.debug("User {} not authorized", request.getPrincipal().getName());
            HttpExchangeUtil.forbidden(httpExchange);
            return;
        }

        Controller controller = controllers.select(request);

        View view = controller.process(request);

        Response response = view.render();

        process(httpExchange, request, response);
    }

    private boolean authenticated(final Request request) {
        return existPrincipal(request) && existUser(request);
    }

    private boolean authorized(final Optional<User> user, final Resource resource) {
        return user.isPresent() && hasPermissions(user.get(), resource);
    }

    private boolean hasPermissions(final User user, final Resource resource) {
        return permissionChecker.hasPermission(user, resource);
    }

    private boolean existUser(final Request request) {
        return userService.findByUsername(request.getPrincipal().getName()).isPresent();
    }

    private boolean existPrincipal(final Request request) {
        return request.getPrincipal() != null;
    }

    private void process(HttpExchange httpExchange, Request request, Response response) throws IOException {
        HttpExchangeUtil.write(httpExchange, response.getStatusCode(), "text/html", response.getContent());
    }
}
