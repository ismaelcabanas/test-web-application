package cabanas.garcia.ismael.opportunity;

import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import cabanas.garcia.ismael.opportunity.server.WebServer;
import cucumber.api.java8.En;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by XI317311 on 02/03/2017.
 */
public class ProcessRequestStepDef implements En {

    private String response;
    private WebServer standardWebServer;
    private int port;

    public ProcessRequestStepDef() {
        Given("^the web server is running on port (\\d+)$", (Integer port) -> {
            this.port = port;
            standardWebServer = new SunHttpServer(port);
            try {
                standardWebServer.start();
            } catch (UnavailableServerException e) {
                throw new RuntimeException(e);
            }
        });

        When("^I send a (.*) request to web server$", (String page) -> {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:" + port + page);
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Then("^the web server returns (.*)$", (String expected) -> {
            Assert.assertThat(response, Is.is(IsEqual.equalTo(expected)));
        });

        After(() -> {
            standardWebServer.stop();
        });
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
