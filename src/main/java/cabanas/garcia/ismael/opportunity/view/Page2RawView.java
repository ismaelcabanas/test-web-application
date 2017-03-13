package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.User;

import java.net.HttpURLConnection;

public class Page2RawView implements WebView {

    private static String rawContent = "Hello %s, you are in %s";

    private User model;

    public Page2RawView(User model) {
        this.model = model;
    }

    public Page2RawView() {
    }

    @Override
    public Response render() {
        String content = String.format(rawContent, model.getUsername(), getName());
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_OK)
                .content(content.getBytes())
                .build();
    }

    @Override
    public String getName() {
        return "PAGE2";
    }
}
