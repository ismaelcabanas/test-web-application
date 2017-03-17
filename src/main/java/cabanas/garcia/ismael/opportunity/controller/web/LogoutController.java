package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
import cabanas.garcia.ismael.opportunity.view.View;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class LogoutController extends Controller{
    protected static final String PATH = "/logout";
    private String redirectPath;
    private SessionManager sessionManager;

    public LogoutController() {
        // Needed for instantiation by reflection
    }

    public LogoutController(SessionManager sessionManager, String redirectPath) {
        this.sessionManager = sessionManager;
        this.redirectPath = redirectPath;
    }

    public LogoutController(SessionManager sessionManager) {
        this(sessionManager, StringUtils.EMPTY);
    }

    @Override
    public View process(Request request) {
        Optional<Session> session = request.getSession();
        if(session.isPresent()){
            Session theSession = session.get();
            sessionManager.invalidate(theSession);
            request.setSession(null);
        }

        return new RedirectView(redirectPath);
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path(PATH).build();
    }
}
