package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class AbstractHttpHandler implements HttpHandler{
    @Override
    public abstract void handle(HttpExchange httpExchange) throws IOException;
}
