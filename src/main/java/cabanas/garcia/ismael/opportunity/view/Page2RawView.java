package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.User;

import java.net.HttpURLConnection;

public class Page2RawView implements WebView {

    private User model;

    public Page2RawView(User model) {
        this.model = model;
    }

    public Page2RawView() {
        // TODO Improve this...
    }

    @Override
    public Response render() {
        String content = String.format(ViewConstants.RAW_CONTENT, model.getUsername(), getName());
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
