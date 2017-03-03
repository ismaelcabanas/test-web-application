package cabanas.garcia.ismael.opportunity.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@Slf4j
public class SunHttpServer implements WebServer {

    private final int port;

    private State state;

    private HttpServer httpServer;

    public SunHttpServer(int port) {
        this.port = port;
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

    private void updateStatus(State newState) {
        this.state = newState;
    }

    private HttpServer createServer() throws UnavailableServerException {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            HttpHandler handler = new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {
                    String response = "Hello";
                    httpExchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream out = httpExchange.getResponseBody();
                    out.write(response.getBytes());
                    out.flush();
                    httpExchange.close();
                }
            };
            server.createContext("/", handler);
            server.setExecutor(null);

        } catch (IOException e) {
            throw new UnavailableServerException(e);
        }
        return server;
    }

}
