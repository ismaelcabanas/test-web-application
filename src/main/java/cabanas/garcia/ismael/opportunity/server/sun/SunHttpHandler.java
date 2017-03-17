package cabanas.garcia.ismael.opportunity.server.sun;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class SunHttpHandler extends AbstractHttpHandler{
    private final Controllers controllers;

    public SunHttpHandler(Controllers controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Request request = RequestFactory.create(httpExchange);

        log.debug("Handle request {}", request);

        Controller controller = controllers.select(request);

        View view = controller.process(request);

        Response response = view.render();

        process(httpExchange, request, response);

    }

    private void process(HttpExchange httpExchange, final Request request, final Response response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");

        if(request.getSession().isPresent())
            addCookieResponseHeader(httpExchange, request);

        if(!response.isRedirect()) {
            HttpExchangeUtil.write(httpExchange, response.getStatusCode(), "text/html", response.getContent());
        }
        else
            HttpExchangeUtil.redirect(httpExchange, response.getRedirectPath());
    }

    private void addCookieResponseHeader(HttpExchange httpExchange, final Request request) {
        log.debug("Adding cookie response header...");
        if(request.getSession().isPresent()){
            Session session = request.getSession().get();
            String cookieValue = String.format("%s=%s", Cookie.SESSION_TOKEN, session.getSessionId());
            log.debug("Setting {} header with value {}", ResponseHeaderConstants.SET_COOKIE, cookieValue);
            httpExchange.getResponseHeaders()
                    .add(ResponseHeaderConstants.SET_COOKIE, cookieValue);
        }
        else
            log.debug("No session cookie set, not exist session");
    }

    private void writeResponse(HttpExchange httpExchange, byte[] response) throws IOException {
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(response);
        responseBody.flush();
        responseBody.close();
    }
}
