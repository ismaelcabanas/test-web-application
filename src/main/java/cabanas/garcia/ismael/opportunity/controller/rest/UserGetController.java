package cabanas.garcia.ismael.opportunity.controller.rest;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.ResourceNotFoundView;
import cabanas.garcia.ismael.opportunity.view.UserDeletedView;
import cabanas.garcia.ismael.opportunity.view.UserGetView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class UserGetController extends Controller{
    private UserService userService;

    public UserGetController() {
        // Needed for instantiation by reflection
    }

    public UserGetController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public View process(Request request) {
        String username = extractUsernameFromRequestPath(request.getPath());

        Optional<User> existentUser = userService.findByUsername(username);

        if(existentUser.isPresent()) {
            userService.delete(username);
            return new UserGetView(existentUser.get());
        }
        else{
            return new ResourceNotFoundView();
        }
    }

    @Override
    public String getMappingPath() {
        return "/users";
    }

    @Override
    public String getMethod() {
        return RequestMethodConstants.GET;
    }

    private String extractUsernameFromRequestPath(String path) {
        String[] pathSplitted = path.split("/");
        return pathSplitted[pathSplitted.length-1];
    }
}
