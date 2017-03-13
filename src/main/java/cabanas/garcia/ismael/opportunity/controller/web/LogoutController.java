package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class LogoutController extends Controller{
    protected static final String PATH = "/logout";
    private SessionRepository sessionRepository;

    public LogoutController() {
        // Needed for instantiation by reflection
    }

    public LogoutController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public View process(Request request) {
        Optional<Session> session = request.getSession();
        if(session.isPresent()){
            Session theSession = session.get();

            sessionRepository.delete(theSession.getSessionId());

            theSession.invalidate();

            request.setSession(theSession);
        }

        return null;
    }

    @Override
    public String getMappingPath() {
        return PATH;
    }
}
