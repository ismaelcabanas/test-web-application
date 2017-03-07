package cabanas.garcia.ismael.opportunity.server.sun;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class SunHttpHandler implements HttpHandler{
    private final Controllers controllers;

    public SunHttpHandler(Controllers controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        Optional<Controller> controller = controllers.select(request);

        if(controller.isPresent()) {
            View view = controller.get().process(request);

            String response = view.render();

            renderResponse(httpExchange, response);
        }
    }

    private void renderResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
