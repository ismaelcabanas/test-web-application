package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.http.imp.DefaultRequest;
import com.sun.net.httpserver.HttpExchange;

public class RequestFactory {

    private RequestFactory(){
    }

    public static Request create(HttpExchange httpExchange) {
        assert httpExchange != null;

        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);

        return
            DefaultRequest.builder()
                    .path(extractorHttpExchange.extractPathFrom())
                    .session(extractorHttpExchange.extractSession())
                    .parameters(extractorHttpExchange.getParameters())
                    .method(extractorHttpExchange.getMethod())
                    .build();
    }


}
