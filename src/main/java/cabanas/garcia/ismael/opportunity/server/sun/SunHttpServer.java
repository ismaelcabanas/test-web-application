package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.server.State;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import cabanas.garcia.ismael.opportunity.server.sun.handlers.RestHandler;
import com.sun.corba.se.spi.activation.Server;
import com.sun.net.httpserver.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SunHttpServer {

    private int port;

    private State state;

    private HttpServer httpServer;
    private ServerConfiguration configuration;

    public SunHttpServer() {
        this.state = State.STOPPED;
        configuration = ServerConfiguration.getInstance();
    }

    public SunHttpServer(int port) {
        this.port = port;
        this.configuration = ServerConfiguration.getInstance();
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
            this.httpServer.setExecutor(null);
            updateStatus(State.STOPPED);
        } catch (IOException e) {
            throw new UnavailableServerException(e);
        }
    }

    public void start() {

        log.info("Starting server...");

        this.httpServer.start();

        updateStatus(State.RUNNING);

        log.info("Server started on port {}", port);
    }

    public boolean isRunning() {
        return state != State.STOPPED;
    }

    public void stop() {
        try {
            if(isRunning()) {
                httpServer.stop(1);
                updateStatus(State.STOPPED);
            }
        }
        finally {
            httpServer = null;
        }
        log.info("Server shutdown");
    }

    private void updateStatus(State newState) {
        this.state = newState;
    }

    public void createContext(String contextPath, HttpHandler handler, List<Filter> filters) {
        createContext(contextPath, handler, filters, Optional.empty());
    }

    public void createContext(String contextPath, HttpHandler handler, List<Filter> filters, Optional<Authenticator> authenticator) {
        HttpContext context = this.httpServer.createContext(contextPath, handler);
        if(authenticator.isPresent())
            context.setAuthenticator(authenticator.get());
        filters.forEach(filter -> context.getFilters().add(filter));
    }

    public ServerConfiguration getConfiguration() {
        return this.configuration;
    }

}
