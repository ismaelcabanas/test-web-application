package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;

import java.net.HttpURLConnection;

public class UnknownResourceRawView implements View{

    private static final String RAW_CONTENT = "<html>\n" +
            "\t<body>\n" +
            "\t<h1>Resource not found!!</h1>\n" +
            "\t<a href=\"/login\">Login\n" +
            "\t</a>\n" +
            "\t</body>\n" +
            "</html>";

    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .content(RAW_CONTENT.getBytes())
                .build();
    }
}
