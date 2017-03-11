package cabanas.garcia.ismael.opportunity.server.sun;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class SunHttpHandler implements HttpHandler{
    private final Controllers controllers;

    public SunHttpHandler(Controllers controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        log.info("Handle request {}", request);

        Controller controller = controllers.select(request);

        View view = controller.process(request);

        Response response = view.render();

        process(httpExchange, response);

    }

    private void process(HttpExchange httpExchange, Response response) throws IOException {
        httpExchange.sendResponseHeaders(response.getStatusCode(), response.getContent().length);
        writeResponse(httpExchange, response.getContent());
    }

    private void writeResponse(HttpExchange httpExchange, byte[] response) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
