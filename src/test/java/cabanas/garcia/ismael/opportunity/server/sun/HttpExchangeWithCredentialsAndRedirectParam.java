package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpExchangeWithCredentialsAndRedirectParam extends HttpExchangeWithCredentials {
    private static final String payload = "username=ismael&" +
            "password=failed&redirect=/page1";
    @Override
    public InputStream getRequestBody() {
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }
}
