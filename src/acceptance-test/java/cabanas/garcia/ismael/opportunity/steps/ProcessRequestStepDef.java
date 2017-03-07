package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cucumber.api.PendingException;
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

import static cabanas.garcia.ismael.opportunity.steps.Hooks.controllerMapper;
import static cabanas.garcia.ismael.opportunity.steps.Hooks.controllerScanner;
import static cabanas.garcia.ismael.opportunity.steps.Hooks.standardWebServer;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ProcessRequestStepDef implements En {

    private int statusCode;
    private String response;
    private int port;

    public ProcessRequestStepDef() {

        Given("^the web server is running on port (\\d+)$", (Integer port) -> {
            this.port = port;
            standardWebServer = new StandardWebServer(port, controllerScanner, controllerMapper, new SunHttpServer());
            standardWebServer.start();
        });

        When("^I send a (.*) request to web server$", (String page) -> {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:" + port + page);
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Then("^the web server returns (.*)$", (String expected) -> {
            assertThat(response, is(equalTo(expected)));
        });
        And("^(\\d+) status code$", (Integer statusCode) -> {
            assertThat(this.statusCode, is(equalTo(statusCode)));
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
