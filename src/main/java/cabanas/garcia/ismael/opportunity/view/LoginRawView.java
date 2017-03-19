package cabanas.garcia.ismael.opportunity.view;

import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

@Slf4j
public class LoginRawView implements View{

    private static final String raw = "<html>\n" +
            "\t<body>\n" +
            "\t<h1>Login</h1>\n" +
            "\t<p>Introduce your credentials</p>\t\n" +
            "\t<form action=\"/login\" method=\"post\">\n" +
            "\t\t<label for=\"user\">User</label>\n" +
            "\t\t<input type=\"text\" name=\"username\">\n" +
            "\t\t<label for=\"password\">Password</label>\n" +
            "\t\t<input type=\"password\" name=\"password\">\n" +
            "\t\t<input type=\"submit\" value=\"Login\">\n" +
            "\t\t<input type=\"hidden\" name=\"redirect\" value=\"%s\">\n" +
            "\t</form>\n" +
            "\t</body>\n" +
            "</html>";

    private String redirectPath;

    private String rawContent;

    public LoginRawView() {
        rawContent = String.format(raw, "");
    }


    public LoginRawView(String redirectPath) {
        this.redirectPath = redirectPath;
        try {
            rawContent = String.format(raw, URLEncoder.encode(redirectPath, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding {}: {}", redirectPath, e.getMessage());
            rawContent = String.format(raw, "");
        }
    }

    @Override
    public Response render() {
        return DefaultResponse.builder()
                .statusCode(HttpURLConnection.HTTP_OK)
                .content(rawContent.getBytes())
                .build();
    }

    public String getRedirectPath() {
        return this.redirectPath;
    }
}
