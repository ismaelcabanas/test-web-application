package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.server.Configuration;
import cabanas.garcia.ismael.opportunity.server.State;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import cabanas.garcia.ismael.opportunity.server.WebServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class SunHttpServer implements WebServer {

    private State state;

    private HttpServer httpServer;

    private Configuration configuration;

    public SunHttpServer() {
        this.state = State.STOPPED;
    }

    @Override
    public void start() throws UnavailableServerException {

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

    private HttpServer createServer() throws UnavailableServerException {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(configuration.getPort()), 0);

            Mapping mapping = configuration.getControllerMapping();

            Controllers controllers = new Controllers(mapping, new ConstructorInstantiator());

            HttpHandler handler = new SunHttpHandler(controllers);

            server.createContext("/", handler);
            server.setExecutor(null);

        } catch (IOException e) {
            throw new UnavailableServerException(e);
        }
        return server;
    }

}
