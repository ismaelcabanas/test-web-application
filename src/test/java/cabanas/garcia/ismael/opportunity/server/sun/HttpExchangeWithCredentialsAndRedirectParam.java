package cabanas.garcia.ismael.opportunity.server.sun;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpExchangeWithCredentialsAndRedirectParam extends HttpExchangeWithCredentialsStub {
    private static final String payload = "username=ismael&" +
            "password=failed&redirect=/page1";
    @Override
    public InputStream getRequestBody() {
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }
}
