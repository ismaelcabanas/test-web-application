package cabanas.garcia.ismael.opportunity;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.DIControllerFactory;
import cabanas.garcia.ismael.opportunity.http.session.DefaultSessionManager;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.security.permission.*;
import cabanas.garcia.ismael.opportunity.server.authenticators.RestBasicAuthenticator;
import cabanas.garcia.ismael.opportunity.server.sun.ServerConfiguration;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthorizationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpHandler;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.server.sun.handlers.RestHandler;
import cabanas.garcia.ismael.opportunity.service.DefaultPrivateResourceService;
import cabanas.garcia.ismael.opportunity.service.DefaultUserService;
import cabanas.garcia.ismael.opportunity.service.PrivateResourcesService;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Filter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Main {

    public static final String CHANGEIT = "changeit";
    private static PermissionChecker permissionChecker;
    private static PrivateResourcesService privateResourcesService;

    private static Role rolePage1 = Role.builder().name("Page1").build();
    private static Role rolePage2 = Role.builder().name("Page2").build();
    private static Role rolePage3 = Role.builder().name("Page3").build();
    private static Role roleAdmin = Role.builder().name("Admin").build();

    private static Roles rolesResourcePage1 = Roles.builder().roleList(Arrays.asList(rolePage1)).build();
    private static Roles rolesResourcePage2 = Roles.builder().roleList(Arrays.asList(rolePage2)).build();
    private static Roles rolesResourcePage3 = Roles.builder().roleList(Arrays.asList(rolePage3)).build();

    private Main(){}

    public static void main(String... args) throws Exception{

        configureSecurity();

        List<Filter> filters = configureFilters();

        loadDefaultUsers();

        SunHttpServer httpServer = new SunHttpServer(8080);
        httpServer.getConfiguration().add(ServerConfiguration.SESSION_TIMEOUT, 60);
        httpServer.getConfiguration().add(ServerConfiguration.REDIRECT_LOGOUT, "/login");

        Controllers webControllers = webControllers();
        SunHttpHandler webHandler = new SunHttpHandler(webControllers);
        httpServer.createContext("/", webHandler, filters);


        Controllers restControllers = restControllers();
        UserService userService = new DefaultUserService(InMemoryUserRepository.getInstance());
        PermissionChecker permissionChecker = new DefaultRestPermissionChecker();
        RestHandler restHandler = new RestHandler(restControllers, userService, permissionChecker);

        UserService userServiceForAuthenticator = new DefaultUserService(InMemoryUserRepository.getInstance());
        BasicAuthenticator basicAuthenticator = new RestBasicAuthenticator("test_web_application", userServiceForAuthenticator);
        httpServer.createContext("/users", restHandler, Collections.emptyList(), Optional.of(basicAuthenticator));

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
        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller.web");
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);
        return new Controllers(controllerMapping, new DIControllerFactory(new ConstructorInstantiator()));
    }

    private static List<Filter> configureFilters() {
        log.info("Configuring filters...");

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration = new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.redirectPath("/login");
        configuration.redirectForbiddenPath("/forbidden");

        SessionManager sessionManager = new DefaultSessionManager(InMemorySessionRepository.getInstance());

        SunHttpAuthorizationFilter authenticationFilter =
                new SunHttpAuthorizationFilter(configuration, sessionManager,
                        permissionChecker,
                        privateResourcesService);

        List<Filter> filters = new ArrayList<>();
        filters.add(authenticationFilter);

        return filters;
    }

    private static void loadDefaultUsers() {

        UserRepository userRepository = InMemoryUserRepository.getInstance();

        User adminUser = getAdminUser();
        userRepository.persist(adminUser);

        User user1 = User.builder().username("user1").password(CHANGEIT).roles(rolesResourcePage1).build();
        userRepository.persist(user1);
        User user2 = User.builder().username("user2").password(CHANGEIT).roles(rolesResourcePage2).build();
        userRepository.persist(user2);
        User user3 = User.builder().username("user3").password(CHANGEIT).roles(rolesResourcePage3).build();
        userRepository.persist(user3);
    }

    private static User getAdminUser() {
        Roles roles = Roles.builder().roleList(Arrays.asList(roleAdmin)).build();

        return User.builder().username("admin").password("admin").roles(roles).build();
    }

    private static void configureSecurity() {
        Resource resourcePage1 = Resource.builder().path("/page1").build();
        Resource resourcePage2 = Resource.builder().path("/page2").build();
        Resource resourcePage3 = Resource.builder().path("/page3").build();

        Permissions permissions = new Permissions();
        permissions.add(Permission.builder().resource(resourcePage1).roles(rolesResourcePage1).build());
        permissions.add(Permission.builder().resource(resourcePage2).roles(rolesResourcePage2).build());
        permissions.add(Permission.builder().resource(resourcePage3).roles(rolesResourcePage3).build());

        log.info("Application permissions set: " + permissions);
        permissionChecker = new DefaultPermissionChecker(permissions);

        PrivateResources privateResources = new PrivateResources();
        privateResources.add(resourcePage1);
        privateResources.add(resourcePage2);
        privateResources.add(resourcePage3);

        log.info("Resources securized: " + privateResources);
        privateResourcesService = new DefaultPrivateResourceService(privateResources);
    }

}
