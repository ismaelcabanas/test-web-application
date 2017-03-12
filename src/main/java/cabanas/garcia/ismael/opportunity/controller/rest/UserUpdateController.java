package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.ResourceNotFoundView;
import cabanas.garcia.ismael.opportunity.view.UserUpdatedView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.ArrayList;
import java.util.Optional;

public class UserUpdateController extends Controller{
    private UserService userService;

    public UserUpdateController() {
        // Need it for instantiation by reflection
    }

    public UserUpdateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public View process(Request request) {
        User userFromRequest = extractUserDataFrom(request);

        Optional<User> existentUser = userService.findByUsername(userFromRequest.getUsername());

        if(existentUser.isPresent()) {
            User userUpdated = userService.update(userFromRequest);
            return new UserUpdatedView(userUpdated);
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
    public String getMethod() {
        return RequestMethodConstants.PUT;
    }

    private User extractUserDataFrom(final Request request) {
        return User.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .roles(getRolesFromRequest(request.getParameter("roles")))
                .build();
    }

    private Roles getRolesFromRequest(String stringOfRoles) {
        Roles roles = Roles.builder().roleList(new ArrayList<>()).build();

        if(stringOfRoles != null){
            String[] rolesSplitted = stringOfRoles.split(",");
            for(int i=0; i<rolesSplitted.length; i++){
                roles.add(rolesSplitted[i]);
            }
        }

        return roles;
    }
}
