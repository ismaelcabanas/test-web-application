package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.getStringFromInputStream;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.port;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.statusCode;
import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.response;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ApiUsersStepDefs implements En {
    private static final String DEFAULT_PASSWORD = "1234";
    private String usernameToFind;
    private String usernameToDelete;
    private String usernameToUpdate;
    private String newRoles;
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
        When("^I use API for creating users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            HttpClient httpClient = HttpUtil.create();
            HttpPost httpPost = new HttpPost("http://localhost:" + port + "/users");

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("username", username));
            urlParameters.add(new BasicNameValuePair("password", password));
            urlParameters.add(new BasicNameValuePair("roles", roles));

            String authHeader = getAuthHeader(authUser, authPassword);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

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

        And("^there is an user in the system called (.*) with role (.*)$", (String username, String role) -> {
            UserRepository userRepository = InMemoryUserRepository.getInstance();
            Roles roles = Roles.builder().roleList(new ArrayList<>()).build();
            roles.add(role);
            User user = User.builder().username(username).password(DEFAULT_PASSWORD).roles(roles).build();
            userRepository.persist(user);
        });
        And("^And I want to update roles to (.*) for user (.*)", (String newRoles, String username) -> {
            this.newRoles = newRoles;
            this.usernameToUpdate = username;
        });
        When("^I use API for updating users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            HttpClient httpClient = HttpUtil.create();
            HttpPut httpPut = new HttpPut("http://localhost:" + port + "/users/" + usernameToUpdate);

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("roles", newRoles));

            String authHeader = getAuthHeader(authUser, authPassword);
            httpPut.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            try {
                httpPut.setEntity(new UrlEncodedFormEntity(urlParameters));
                HttpResponse httpResponse = httpClient.execute(httpPut);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        And("^And I want to delete user (.*)$", (String username) -> {
            this.usernameToDelete = username;
        });
        When("^I use API for deleting users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            HttpClient httpClient = HttpUtil.create();
            HttpDelete httpDelete = new HttpDelete("http://localhost:" + port + "/users/" + usernameToDelete);

            String authHeader = getAuthHeader(authUser, authPassword);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            try {
                HttpResponse httpResponse = httpClient.execute(httpDelete);
                statusCode = httpResponse.getStatusLine().getStatusCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        And("^And I want to get data for user (.*)", (String username) -> {
            this.usernameToFind = username;
        });
        When("^I use API for getting users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            HttpClient httpClient = HttpUtil.create();
            HttpGet httpGet = new HttpGet("http://localhost:" + port + "/users/" + usernameToFind);

            String authHeader = getAuthHeader(authUser, authPassword);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                response = getStringFromInputStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private String getAuthHeader(String authUser, String authPassword) {
        String auth = authUser + ":" + authPassword;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("ISO-8859-1")));
        return "Basic " + new String(encodedAuth);
    }
}
