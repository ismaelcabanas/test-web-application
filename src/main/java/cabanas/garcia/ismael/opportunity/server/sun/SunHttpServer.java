package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.DIControllerFactory;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.server.Configuration;
import cabanas.garcia.ismael.opportunity.server.State;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import cabanas.garcia.ismael.opportunity.server.WebServer;
import cabanas.garcia.ismael.opportunity.service.DefaultUserService;
import cabanas.garcia.ismael.opportunity.service.UserService;
import com.sun.net.httpserver.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class SunHttpServer implements WebServer {

    private static SunHttpServer instance;

    private State state;

    private HttpServer httpServer;

    private Configuration configuration;

    public SunHttpServer() {
        this.state = State.STOPPED;
    }

    @Override
    public void start() {

        httpServer = createServer();

        httpServer.start();

        updateStatus(State.RUNNING);

        log.info("Server started");
    }

    @Override
    public boolean isRunning() {
        return state != State.STOPPED;
    }

    @Override
    public void stop() {
        try {
            httpServer.stop(1);
            updateStatus(State.STOPPED);
        }
        finally {
            httpServer = null;
        }
        log.info("Server shutdown");
    }

    @Override
    public void addConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private void updateStatus(State newState) {
        this.state = newState;
    }

    private HttpServer createServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(configuration.getPort()), 0);

            Mapping mapping = configuration.getControllerMapping();

            Controllers controllers = new Controllers(mapping, new DIControllerFactory(new ConstructorInstantiator()));

            HttpHandler handler = new SunHttpHandler(controllers);

            HttpContext rootContext = server.createContext("/", handler);

            SunHttpAuthenticationFilter authenticationFilter = new SunHttpAuthenticationFilter();
            authenticationFilter.getConfiguration().addPrivateResource("/page1");
            authenticationFilter.getConfiguration().addPrivateResource("/page2");
            authenticationFilter.getConfiguration().addPrivateResource("/page3");
            authenticationFilter.getConfiguration().redirectPath("/login");

            rootContext.getFilters().add(authenticationFilter);

            HttpContext userAPIContext = server.createContext("/users", handler);
            BasicAuthenticator restBasicAuthenticator = new BasicAuthenticator("testwebapplication") {
                @Override
                public boolean checkCredentials (String username, String password) {
                    UserService userService = new DefaultUserService(InMemoryUserRepository.getInstance());
                    return userService.login(username, password).isPresent();
                }
            };
            userAPIContext.setAuthenticator(restBasicAuthenticator);
            server.setExecutor(null);

        } catch (IOException e) {
            throw new UnavailableServerException(e);
        }
        return server;
    }

    public static SunHttpServer getInstance() {
        if(instance == null){
            instance = new SunHttpServer();
        }
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
