package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;

/**
 * Created by XI317311 on 16/03/2017.
 */
public class HttpExchangeWithQueryStringParamStub extends HttpExchangeSuccessResourceStub {

    private final String paramValue;
    private final String paramName;

    public HttpExchangeWithQueryStringParamStub(String path, String paramName, String paramValue) {
        super(path);
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public URI getRequestURI() {
        return URI.create(super.getRequestURI().getPath() + "?" + paramName + "=" + paramValue);
    }
}
