package cabanas.garcia.ismael.opportunity.server.sun;

import java.net.URI;

public class HttpExchangeSuccessResourceStub extends HttpExchangeStub {

    private String path;

    public HttpExchangeSuccessResourceStub(String path) {
        super();
        this.path = path;
    }

    @Override
    public URI getRequestURI() {
        return URI.create(path);
    }

    @Override
    public String getRequestMethod() {
        return "GET";
    }


    @Override
    public int getResponseCode() {
        return 200;
    }


}
