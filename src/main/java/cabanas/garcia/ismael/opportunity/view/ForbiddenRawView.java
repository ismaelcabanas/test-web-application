package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;

import java.net.HttpURLConnection;

public class ForbiddenRawView implements WebView {
    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .content("Forbidden Access!!!".getBytes())
                .build();
    }

    @Override
    public String getName() {
        return "FORBIDDEN";
    }
}
