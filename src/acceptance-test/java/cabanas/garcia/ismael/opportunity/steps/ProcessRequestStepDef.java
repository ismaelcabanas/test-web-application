package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class ProcessRequestStepDef implements En {

    protected int statusCode;
    protected String response;
    protected int port;

    private String username;
    private String password;

    public ProcessRequestStepDef() {

        Given("^the web server is running on port (\\d+)$", (Integer port) -> {
            this.port = port;
            httpServer = new SunHttpServer(port);
            httpServer.start();
        });

        When("^I send a (.*) request to web server$", (String page) -> {
            HttpClient httpClient = HttpUtil.create();
            HttpGet httpGet = new HttpGet("http://localhost:" + port + page);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Then("^the web server returns (.*) resource$", (String expected) -> {
            assertThat(this.response, containsString(expected));
        });
        And("^(\\d+) status code$", (Integer statusCode) -> {
            assertThat(this.statusCode, is(equalTo(statusCode)));
        });
        Then("^the web server redirects me to login page$", () -> {
            assertThat(this.statusCode, is(equalTo(200)));
            assertThat(this.response, containsString("Login"));
        });
        And("^if I login with user (.*) and password (.*) successfully$", (String user, String password) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        Given("^credentials (.*)/(.*)", (String user, String password) -> {
            this.username = user;
            this.password = password;
        });
        When("^I send try to login to web server$", () -> {
            HttpClient httpClient = HttpUtil.create();
            HttpPost httpPost = new HttpPost("http://localhost:" + port + "/login");
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("username", username));
            urlParameters.add(new BasicNameValuePair("password", password));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    protected static String getStringFromInputStream(InputStream is) {

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
