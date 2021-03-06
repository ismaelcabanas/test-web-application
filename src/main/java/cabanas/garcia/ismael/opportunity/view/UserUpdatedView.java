package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.User;

import java.net.HttpURLConnection;

public class UserUpdatedView implements View {
    private final User user;

    public UserUpdatedView(User user) {
        this.user = user;
    }

    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_OK)
                .model(user)
                .content(user.toString().getBytes())
                .build();
    }
}
