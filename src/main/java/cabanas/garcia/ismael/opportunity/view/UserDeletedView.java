package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;

import java.net.HttpURLConnection;

public class UserDeletedView implements View {
    @Override
    public Response render() {
        return DefaultResponse.builder().statusCode(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
