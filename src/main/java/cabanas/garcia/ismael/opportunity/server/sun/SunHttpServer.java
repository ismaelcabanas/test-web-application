package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.server.State;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import com.sun.corba.se.spi.activation.Server;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

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

        this.httpServer.start();

        updateStatus(State.RUNNING);

        log.info("Server started");
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

    public void createContext(String contextPath, SunHttpHandler handler, List<Filter> filters) {
        HttpContext context = this.httpServer.createContext(contextPath, handler);
        filters.forEach(filter -> context.getFilters().add(filter));
    }
    public void createContext(String contextPath, SunHttpHandler handler, BasicAuthenticator authenticator) {
        HttpContext context = this.httpServer.createContext(contextPath, handler);
        context.setAuthenticator(authenticator);
    }

    public ServerConfiguration getConfiguration() {
        return this.configuration;
    }
}
