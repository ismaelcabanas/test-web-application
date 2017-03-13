package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.HomeRawView;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
import cabanas.garcia.ismael.opportunity.view.UnAuthorizedRawView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class LoginPostController extends Controller {

    public static final String PATH = "/login";

    private SessionRepository sessionRepository;

    private UserService userService;

    public LoginPostController() {
        // Necessary for instantiations by reflection
    }

    public LoginPostController(UserService userService, SessionRepository sessionRepository) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public View process(Request request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> user = userService.login(username, password);

        if(user.isPresent()){
            createSession(user.get(), request);
            if(redirect(request)){
                return new RedirectView();
            }
            return new HomeRawView();
        }
        else{
            return new UnAuthorizedRawView();
        }
    }

    private void createSession(final User user, Request request) {
        Session session = Session.create(user);
        request.setSession(session);

        sessionRepository.persist(session);
    }

    private boolean redirect(Request request) {
        return request.getParameter("redirect") != null;
    }

    @Override
    public String getMappingPath() {
        return PATH;
    }

    @Override
    public String getMethod() {
        return RequestMethodConstants.POST;
    }
}
