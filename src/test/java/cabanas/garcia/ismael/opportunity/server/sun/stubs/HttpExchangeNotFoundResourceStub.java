package cabanas.garcia.ismael.opportunity.server.sun.stubs;

import java.net.HttpURLConnection;

/**
 * Created by XI317311 on 07/03/2017.
 */
public class HttpExchangeNotFoundResourceStub extends HttpExchangeSuccessResourceStub {

    public HttpExchangeNotFoundResourceStub(String path) {
        super(path);
    }

    @Override
    public int getResponseCode() {
        return HttpURLConnection.HTTP_NOT_FOUND;
    }
}
