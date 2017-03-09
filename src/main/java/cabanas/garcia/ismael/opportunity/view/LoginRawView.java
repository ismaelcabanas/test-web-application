package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;

import java.net.HttpURLConnection;

public class LoginRawView implements View{

    private static final String raw = "<html>\n" +
            "\t<body>\n" +
            "\t<h1>Login</h1>\n" +
            "\t<p>Introduce your credentials</p>\t\n" +
            "\t<form action=\"/login\" method=\"post\">\n" +
            "\t\t<label for=\"user\">User</label>\n" +
            "\t\t<input type=\"text\" name=\"user\">\n" +
            "\t\t<label for=\"password\">Password</label>\n" +
            "\t\t<input type=\"password\" name=\"password\">\n" +
            "\t\t<input type=\"submit\" value=\"Login\">\n" +
            "\t</form>\n" +
            "\t</body>\n" +
            "</html>";
    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_OK)
                .content(raw.getBytes())
                .build();
    }
}
