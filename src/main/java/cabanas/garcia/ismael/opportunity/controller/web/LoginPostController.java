package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestConstants;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.HomeRawView;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
import cabanas.garcia.ismael.opportunity.view.UnAuthorizedRawView;
import cabanas.garcia.ismael.opportunity.view.View;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Slf4j
public class LoginPostController extends Controller {

    protected static final String PATH = "/login";

    private int sessionTimeout;

    private SessionManager sessionManager;

    private UserService userService;

    public LoginPostController() {
        // Necessary for instantiations by reflection
    }

    public LoginPostController(UserService userService, SessionManager sessionManager) {
        this(userService, sessionManager, -1);
    }

    public LoginPostController(UserService userService, SessionManager sessionManager, Integer sessionTimeout) {
        this.userService = userService;
        this.sessionManager = sessionManager;
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

            Session session = sessionManager.create(user.get(), sessionTimeout);

            log.debug("Session created {}", session);

            request.setSession(session);

            if(request.hasRedirectParameter()){
                log.debug("The request had redirect parameter, then redirect it");
                try {
                    return new RedirectView(URLDecoder.decode(request.getParameter(RequestConstants.REDIRECCT_PARAM), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError(e);
                }
            }
            log.debug("Going to home view");
            return new HomeRawView();
        }
        else{
            log.debug("Authentication failed, wrong credentials. Redirecting to unauthorized page");
            return new UnAuthorizedRawView();
        }
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path(PATH).build();
    }

    @Override
    public RequestMethodEnum getMethod() {
        return RequestMethodEnum.POST;
    }
}
