package cabanas.garcia.ismael.opportunity.steps;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.DIControllerFactory;
import cabanas.garcia.ismael.opportunity.http.session.DefaultSessionManager;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthorizationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import com.sun.net.httpserver.Filter;
import cucumber.api.java8.En;

import java.util.ArrayList;
import java.util.List;

public class Hooks implements En{
    public static SunHttpServer httpServer;
    public final static Controllers webControllers = webControllers();
    public final static Controllers restControllers = restControllers();
    public static List<Filter> filters = null;

    public Hooks(){

        After(() -> {
            if(httpServer.isRunning())
                httpServer.stop();

            UserRepository userRepository = InMemoryUserRepository.getInstance();
            userRepository.deleteAll();
        });

        Before(() -> {
            filters = new ArrayList<>();
        });
    }
    private static Controllers restControllers() {
        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller.rest");
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);
        return new Controllers(controllerMapping, new DIControllerFactory(new ConstructorInstantiator()));
    }

    private static Controllers webControllers() {
        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller.web");
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);
        return new Controllers(controllerMapping, new DIControllerFactory(new ConstructorInstantiator()));
    }


    private static void loadDefaultUsers() {
        UserRepository userRepository = InMemoryUserRepository.getInstance();
    }

}
