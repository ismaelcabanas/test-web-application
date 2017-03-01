package cabanas.garcia.ismael.opportunity.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StandardWebServer implements WebServer {
    private final int port;

    private State state;

    private HttpServer httpServer;

    public StandardWebServer(int port) {
        this.port = port;
        this.state = State.STOPPED;
    }

    public void start() {
        httpServer = createServer();
        httpServer.start();
        updateStatus(State.RUNNING);
    }

    public boolean isRunning() {
        return state != State.STOPPED;
    }

    public void stop() {
        try {
            httpServer.stop(1);
            updateStatus(State.STOPPED);
        }
        finally {
            httpServer = null;
        }
    }

    private void updateStatus(State newState) {
        this.state = newState;
    }

    private HttpServer createServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            HttpHandler handler = new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {

                }
            };
            server.createContext("/", handler);
            server.setExecutor(null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

}
