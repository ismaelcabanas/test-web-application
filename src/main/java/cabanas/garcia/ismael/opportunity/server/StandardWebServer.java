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

        httpServer = createServer(port);
        httpServer.start();
        this.state = State.RUNNING;
    }

    public boolean isRunning() {
        return state != State.STOPPED;
    }

    public void stop() {
        httpServer.stop(1);
        state = State.STOPPED;
    }

    private HttpServer createServer(int port){
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            HttpHandler handler = new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {

                }
            };
            httpServer.createContext("/", handler);
            httpServer.setExecutor(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpServer;
    }
}
