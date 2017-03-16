package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;

@Slf4j
public final class HttpExchangeUtil {

    private HttpExchangeUtil(){}

    public static void redirect(HttpExchange httpExchange, String redirectPath) throws IOException {
        log.debug("Redirecting to {}", redirectPath);
        httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, redirectPath);
        httpExchange.getResponseHeaders().remove(ResponseHeaderConstants.SET_COOKIE);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }
}
