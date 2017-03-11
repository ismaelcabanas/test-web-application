package cabanas.garcia.ismael.opportunity.steps;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.DIControllerFactory;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.authenticators.RestBasicAuthenticator;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthenticationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpHandler;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Filter;
import cucumber.api.java8.En;

import java.util.ArrayList;
import java.util.List;

public class Hooks implements En{
    public static SunHttpServer httpServer;
    public final static Controllers webControllers = webControllers();
    public final static Controllers restControllers = restControllers();
    public final static List<Filter> filters = configureFilters();

    public Hooks(){

        After(() -> {
            if(httpServer.isRunning())
                httpServer.stop();
        });

        Before(() -> {
            /*configurePermissions();

            List<Filter> filters = configureFilters();

            loadDefaultUsers();

            httpServer = new SunHttpServer(8082);

            Controllers webControllers = webControllers();
            SunHttpHandler webHandler = new SunHttpHandler(webControllers);
            httpServer.createContext("/", webHandler, filters);

            Controllers restControllers = restControllers();
            SunHttpHandler restHandler = new SunHttpHandler(restControllers);
            BasicAuthenticator basicAuthenticator = new RestBasicAuthenticator("test_web_application");
            httpServer.createContext("/users", restHandler, basicAuthenticator);

            httpServer.start();*/
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

    private static List<Filter> configureFilters() {
        SunHttpAuthenticationFilter authenticationFilter = new SunHttpAuthenticationFilter();
        authenticationFilter.getConfiguration().addPrivateResource("/page1");
        authenticationFilter.getConfiguration().addPrivateResource("/page2");
        authenticationFilter.getConfiguration().addPrivateResource("/page3");
        authenticationFilter.getConfiguration().redirectPath("/login");

        List<Filter> filters = new ArrayList<>();
        filters.add(authenticationFilter);

        return filters;
    }

    private static void loadDefaultUsers() {
        UserRepository userRepository = InMemoryUserRepository.getInstance();
    }

    private static void configurePermissions() {

    }
}
