package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.getStringFromInputStream;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.port;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.statusCode;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.response;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by XI317311 on 10/03/2017.
 */
public class ApiUsersStepDefs implements En {
    private String passwordForAuthentication;
    private String usernameForAuthentication;
    private String roles;
    private String password;
    private String username;

    public ApiUsersStepDefs() {
        And("^I want create user with name (.*)", (String username) -> {
            this.username = username;
        });
        And("^with password (.*)$", (String password) -> {
            this.password = password;
        });
        And("^with roles (.*)$", (String roles) -> {
            this.roles = roles;
        });
        When("^I use API for creating users$", () -> {
            HttpClient httpClient = HttpUtil.create();
            HttpPost httpPost = new HttpPost("http://localhost:" + port + "/users");
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
        Then("^the web server returns (\\d+) status code$", (Integer statusCodeExpected) -> {
            assertThat(statusCode, is(equalTo(statusCodeExpected)));
        });
        And("^there is the user (.*)/(.*) with (.*) role in the system$", (String username, String password, String rolename) -> {
            UserRepository userRepository = InMemoryUserRepository.getInstance();
            Roles roles = Roles.builder().roleList(new ArrayList<>()).build();
            roles.add(rolename);
            User newUser = User.builder().username(username).password(password).roles(roles).build();
            userRepository.persist(newUser);
        });
        And("^I authenticate with user (.*) and password (.*)$", (String username, String password) -> {
            this.usernameForAuthentication = username;
            this.passwordForAuthentication = password;
        });
    }
}
