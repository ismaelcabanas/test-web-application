package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public final class HttpExchangeUtil {

    private HttpExchangeUtil(){}

    public static void redirect(HttpExchange httpExchange, String redirectPath) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, redirectPath);
        httpExchange.getResponseHeaders().remove(ResponseHeaderConstants.SET_COOKIE);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }
}
