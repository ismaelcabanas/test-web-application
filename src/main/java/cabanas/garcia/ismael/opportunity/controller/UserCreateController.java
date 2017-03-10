package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.UserCreatedView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.ArrayList;
import java.util.List;

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
        return User.builder()
                    .username(request.getParameter("username"))
                    .password(request.getParameter("password"))
                    .roles(getRolesFromRequest(request.getParameter("roles")))
                    .build();
    }

    private Roles getRolesFromRequest(String stringOfRoles) {
        Roles roles = Roles.builder().roleList(new ArrayList<>()).build();

        if(stringOfRoles != null){
            List<Role> rolesList = new ArrayList<>();
            String[] rolesSplitted = stringOfRoles.split(",");
            for(int i=0; i<rolesSplitted.length; i++){
                roles.add(rolesSplitted[i]);
            }
        }

        return roles;
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
