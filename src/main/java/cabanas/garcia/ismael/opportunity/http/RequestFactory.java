package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.imp.DefaultRequest;
import com.sun.net.httpserver.HttpExchange;

public class RequestFactory {

    private RequestFactory(){}

    public static Request create(HttpExchange httpExchange) {
        return DefaultRequest.builder()
                .path(extractPathFrom(httpExchange))
                .build();
    }

    private static String extractPathFrom(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().getPath();
    }
}
