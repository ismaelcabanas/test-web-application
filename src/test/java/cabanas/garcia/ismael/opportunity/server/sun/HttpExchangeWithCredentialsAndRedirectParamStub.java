package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
public class HttpExchangeWithCredentialsAndRedirectParamStub extends HttpExchangeWithCredentialsStub {

    private final String username;
    private final String password;
    private final String redirectPath;

    public HttpExchangeWithCredentialsAndRedirectParamStub(String username, String password, String redirectPath) {
        this.username = username;
        this.password = password;
        this.redirectPath = redirectPath;
    }

    @Override
    public InputStream getRequestBody() {
        String payload = "";
        try {
            payload = "username=" + username + "&" +
                    "password=" + password + "&" + Request.REDIRECCT_PARAM + "=" + URLEncoder.encode(redirectPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding", e);
        }
        InputStream is = new ByteArrayInputStream(payload.getBytes());
        return is;
    }
}
