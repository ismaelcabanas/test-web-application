package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.ResourceNotFoundView;
import cabanas.garcia.ismael.opportunity.view.UserDeletedView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class UserDeleteController extends Controller{
    private UserService userService;

    public UserDeleteController() {
        // Need for instantiation by reflection
    }

    public UserDeleteController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public View process(Request request) {
        String username = extractUsernameFromRequestPath(request.getPath());

        Optional<User> existentUser = userService.findByUsername(username);

        if(existentUser.isPresent()) {
            userService.delete(username);
            return new UserDeletedView();
        }
        else{
            return new ResourceNotFoundView();
        }
    }

    @Override
    public String getMappingPath() {
        return "^/users/.*";
    }

    @Override
    public RequestMethodEnum getMethod() {
        return RequestMethodEnum.DELETE;
    }

    private String extractUsernameFromRequestPath(String path) {
        String[] pathSplitted = path.split("/");
        return pathSplitted[pathSplitted.length-1];
    }

}
