package cabanas.garcia.ismael.opportunity;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.DIControllerFactory;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.model.RoleEnum;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.authenticators.RestBasicAuthenticator;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthenticationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpHandler;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Filter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Main {

    private Main(){}

    public static void main(String... args) throws Exception{

        configurePermissions();

        List<Filter> filters = configureFilters();

        loadDefaultUsers();

        SunHttpServer httpServer = new SunHttpServer(8080);
        httpServer.getConfiguration().add("session_timeout", 60000);

        Controllers webControllers = webControllers();
        SunHttpHandler webHandler = new SunHttpHandler(webControllers);
        httpServer.createContext("/", webHandler, filters);


        Controllers restControllers = restControllers();
        SunHttpHandler restHandler = new SunHttpHandler(restControllers);
        BasicAuthenticator basicAuthenticator = new RestBasicAuthenticator("test_web_application");
        httpServer.createContext("/users", restHandler, basicAuthenticator);

        log.info("Starting server...");
        httpServer.start();
    }

    private static Controllers restControllers() {
        log.info("Scanning rest controllers...");

        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller.rest");
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);
        return new Controllers(controllerMapping, new DIControllerFactory(new ConstructorInstantiator()));
    }

    private static Controllers webControllers() {
        log.info("Scanning web controllers...");

        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller.web");
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);
        return new Controllers(controllerMapping, new DIControllerFactory(new ConstructorInstantiator()));
    }

    private static List<Filter> configureFilters() {
        log.info("Configuring filters...");

        SunHttpAuthenticationFilter authenticationFilter = new SunHttpAuthenticationFilter(InMemorySessionRepository.getInstance());
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

        User adminUser = getAdminUser();

        userRepository.persist(adminUser);
        log.info("User {} persited", adminUser);
    }

    private static User getAdminUser() {
        Roles roles = Roles.builder().roleList(new ArrayList<>()).build();
        roles.add(RoleEnum.ADMIN.getRoleName());

        return User.builder().username("admin").password("admin").roles(roles).build();
    }

    private static void configurePermissions() {
        log.info("Permissions configured");
    }

}
