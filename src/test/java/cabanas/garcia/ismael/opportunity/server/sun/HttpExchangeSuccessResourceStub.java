package cabanas.garcia.ismael.opportunity.server.sun;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

public class HttpExchangeSuccessResourceStub extends HttpExchangeStub {

    private String path;
    private OutputStream baos;

    public HttpExchangeSuccessResourceStub(String path) {
        super();
        this.path = path;
        baos = new ByteArrayOutputStream();
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
