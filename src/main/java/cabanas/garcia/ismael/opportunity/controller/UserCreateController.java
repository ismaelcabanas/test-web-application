package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.UserCreatedView;
import cabanas.garcia.ismael.opportunity.view.View;

public class UserCreateController extends Controller{
    private UserService userService;

    public UserCreateController() {
    }

    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public View process(final Request request) {
        User newUser = extractUserDataFrom(request);

        User user = userService.create(newUser);

        return new UserCreatedView(user);
    }

    private User extractUserDataFrom(final Request request) {
        return null;
    }

    @Override
    public String getMappingPath() {
        return "/users";
    }

    @Override
    public String getMethod() {
        return RequestMethodConstants.POST;
    }
}
