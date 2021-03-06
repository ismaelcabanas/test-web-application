package cabanas.garcia.ismael.opportunity.server.sun.handlers;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.util.HttpExchangeUtil;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class SunHttpHandler extends AbstractHttpHandler {
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

        Optional<Session> session = request.getSession();
        if(session.isPresent()) {
            log.debug("Setting session cookie");
            addCookieResponseHeader(httpExchange, session.get());
        }

        if(!response.isRedirect()) {
            HttpExchangeUtil.write(httpExchange, response.getStatusCode(), "text/html", response.getContent());
        }
        else
            HttpExchangeUtil.redirect(httpExchange, response.getRedirectPath());
    }

    private void addCookieResponseHeader(HttpExchange httpExchange, final Session session) {
        log.debug("Adding cookie response header...");

        String cookieValue = String.format("%s=%s", Cookie.SESSION_TOKEN, session.getSessionId());
        log.debug("Setting {} header with value {}", ResponseHeaderConstants.SET_COOKIE, cookieValue);
        httpExchange.getResponseHeaders().add(ResponseHeaderConstants.SET_COOKIE, cookieValue);
    }

}
