package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.User;

import java.net.HttpURLConnection;

public class Page1RawView implements WebView {

    private User model;

    public Page1RawView(User user) {
        this.model = user;
    }

    public Page1RawView() {
        // TODO Improve this...
    }

    @Override
    public Response render() {
        String content = String.format(RAW_CONTENT, model.getUsername(), getName());
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_OK)
                .content(content.getBytes())
                .build();
    }

    @Override
    public String getName() {
        return "PAGE1";
    }
}
