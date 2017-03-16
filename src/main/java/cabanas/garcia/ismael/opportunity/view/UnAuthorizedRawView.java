package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;

import java.net.HttpURLConnection;

public class UnAuthorizedRawView implements WebView{

    String RAW_CONTENT = "<html>\n" +
            "\t<body>\n" +
            "\t<h1>Unauthorized!!</h1>\n" +
            "\t<a href=\"/login\">Login\n" +
            "\t</a>\n" +
            "\t</body>\n" +
            "</html>";

    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .content(RAW_CONTENT.getBytes())
                .build();
    }

    @Override
    public String getName() {
        return "UNAUTHRORIZED";
    }
}
