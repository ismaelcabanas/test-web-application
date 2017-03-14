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
import cabanas.garcia.ismael.opportunity.server.sun.ServerConfiguration;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthorizationFilter;
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
        httpServer.getConfiguration().add(ServerConfiguration.SESSION_TIMEOUT, 60000);
        httpServer.getConfiguration().add(ServerConfiguration.REDIRECT_LOGOUT, "/login");

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

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration = new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        configuration.addPrivateResource("/page2");
        configuration.addPrivateResource("/page3");
        configuration.redirectPath("/login");

        SunHttpAuthorizationFilter authenticationFilter = new SunHttpAuthorizationFilter(configuration, InMemorySessionRepository.getInstance());

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
