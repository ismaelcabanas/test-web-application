package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.steps.model.Response;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import cucumber.api.java8.En;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static cabanas.garcia.ismael.opportunity.steps.ProcessRequestStepDef.port;
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

    Response httpResposne;

    public ApiUsersStepDefs() {

        Given("^I want to update roles to (.*) for user (.*)", (String newRoles, String username) -> {
            this.newRoles = newRoles;
            this.usernameToUpdate = username;
        });
        Given("^I want to delete user (.*)$", (String username) -> {
            this.usernameToDelete = username;
        });

        Given("^I want to get data for user (.*)", (String username) -> {
            this.usernameToFind = username;
        });

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
            String createUri = "http://localhost:" + port + "/users";
            HttpUtil httpClientUtil = new HttpUtil(createUri);

            httpClientUtil.addFormParameter("username", username);
            httpClientUtil.addFormParameter("password", password);
            httpClientUtil.addFormParameter("roles", roles);

            httpClientUtil.addAuthorizationHeader(authUser, authPassword);

            try {
                httpResposne = httpClientUtil.sendPost();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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

        When("^I use API for updating users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            String updateUri = "http://localhost:" + port + "/users/" + usernameToUpdate;
            HttpUtil httpClientUtil = new HttpUtil(updateUri);

            httpClientUtil.addFormParameter("roles", newRoles);
            httpClientUtil.addAuthorizationHeader(authUser, authPassword);

            try {
                httpResposne = httpClientUtil.sendPut();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        When("^I use API for deleting users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            String deleteUri = "http://localhost:" + port + "/users/" + usernameToDelete;
            HttpUtil httpClientUtil = new HttpUtil(deleteUri);

            httpClientUtil.addAuthorizationHeader(authUser, authPassword);

            try {
                httpResposne = httpClientUtil.sendDelete();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



        When("^I use API for getting users with user (.*) and password (.*)$", (String authUser, String authPassword) -> {
            String getUri = "http://localhost:" + port + "/users/" + usernameToFind;
            HttpUtil httpClientUtil = new HttpUtil(getUri);

            httpClientUtil.addAuthorizationHeader(authUser, authPassword);

            try {
                httpResposne = httpClientUtil.sendGet();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Then("^the web server returns (\\d+) status code$", (Integer statusCodeExpected) -> {
            assertThat(httpResposne.getStatusCode(), is(equalTo(statusCodeExpected)));
        });

    }

    private String getAuthHeader(String authUser, String authPassword) {
        String auth = authUser + ":" + authPassword;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("ISO-8859-1")));
        return "Basic " + new String(encodedAuth);
    }
}
