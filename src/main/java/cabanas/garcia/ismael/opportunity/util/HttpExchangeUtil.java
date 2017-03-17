package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class HttpExchangeUtil {

    private HttpExchangeUtil(){}

    public static void redirect(HttpExchange httpExchange, String redirectPath) throws IOException {
        log.debug("Redirecting to {}", redirectPath);
        httpExchange.getResponseHeaders().add(ResponseHeaderConstants.LOCATION, redirectPath);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
        httpExchange.close();
    }

    public static void write(HttpExchange httpExchange, int statusCode, String contentType, byte[] content) throws IOException {
        if (contentType != null) {
            httpExchange.getResponseHeaders().set("Content-Type", contentType);
        }

        if (content == null) {
            httpExchange.sendResponseHeaders(statusCode, 0);
        }
        else{
            httpExchange.sendResponseHeaders(statusCode, content.length);
        }

        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content);
        responseBody.flush();
        responseBody.close();
        httpExchange.close();
    }

    public static Map<String, String> parseQueryParameters(final HttpExchange httpExchange) {
        Map<String, String> queryParameters = new HashMap<>();

        String query = httpExchange.getRequestURI().getQuery();

        if(query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1) {
                    queryParameters.put(pair[0], pair[1]);
                } else {
                    queryParameters.put(pair[0], "");
                }
            }
        }

        return Collections.unmodifiableMap(queryParameters);
    }

    public static void unauthorized(HttpExchange httpExchange) throws IOException {
        log.debug("Unauthorized request");
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
        httpExchange.close();
    }

    public static void forbidden(HttpExchange httpExchange) throws IOException {
        log.debug("Forbidden request");
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
        httpExchange.close();
    }
}
