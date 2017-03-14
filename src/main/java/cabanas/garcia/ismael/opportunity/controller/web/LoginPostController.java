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
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoginPostController extends Controller {

    protected static final String PATH = "/login";

    private int sessionTimeout;

    private SessionRepository sessionRepository;

    private UserService userService;

    public LoginPostController() {
        // Necessary for instantiations by reflection
    }

    public LoginPostController(UserService userService, SessionRepository sessionRepository) {
        this(userService, sessionRepository, -1);
    }

    public LoginPostController(UserService userService, SessionRepository sessionRepository, Integer sessionTimeout) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public View process(Request request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.debug("Authenticating user {} ...", username);
        Optional<User> user = userService.login(username, password);

        if(user.isPresent()){
            log.debug("Login successfully");
            createSession(user.get(), request);
            if(request.hasRedirectParameter()){
                log.debug("The request had redirect parameter, then redirect it");
                return new RedirectView("/login");
            }
            log.debug("Going to home view");
            return new HomeRawView();
        }
        else{
            log.debug("Authentication failed, redirecting to unauthorized page");
            return new UnAuthorizedRawView();
        }
    }

    private void createSession(final User user, Request request) {
        Session session = Session.create(user, sessionTimeout);

        log.debug("Session created {}", session);

        request.setSession(session);

        sessionRepository.persist(session);
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
