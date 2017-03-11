package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.HomeRawView;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
import cabanas.garcia.ismael.opportunity.view.UnAuthorizedRawView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class LoginPostController extends Controller {

    private UserService userService;

    public LoginPostController() {
    }

    public LoginPostController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public View process(Request request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> user = userService.login(username, password);

        if(isValidUser(user)){
            if(redirect(request)){
                return new RedirectView();
            }
            return new HomeRawView();
        }
        else{
            return new UnAuthorizedRawView();
        }
    }

    private boolean redirect(Request request) {
        return request.getParameter("redirect") != null;
    }

    private boolean isValidUser(Optional<User> user) {
        return user.isPresent();
    }

    @Override
    public String getMappingPath() {
        return "/login";
    }

    @Override
    public String getMethod() {
        return RequestMethodConstants.POST;
    }
}
