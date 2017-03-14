package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.permission.Permission;
import cabanas.garcia.ismael.opportunity.permission.Permissions;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.server.authenticators.RestBasicAuthenticator;
import cabanas.garcia.ismael.opportunity.server.sun.ServerConfiguration;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpHandler;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.steps.model.PermissionData;
import cabanas.garcia.ismael.opportunity.steps.model.UserData;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import com.sun.net.httpserver.BasicAuthenticator;
import cucumber.api.java8.En;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class ProcessRequestStepDef implements En {

    static int statusCode;
    static String response;
    static int port;

    private String username;
    private String password;
    private Header sessionTokenHeader;

    public ProcessRequestStepDef() {

        Given("^the web server is running on port (\\d+)$", (Integer port) -> {
            this.port = port;
            httpServer = new SunHttpServer(port);

            // add permissions?
            addPermissionsToServer(StartServerStepDefs.permissions, httpServer.getConfiguration().getPermissions());

            // add users?
            addUsers(StartServerStepDefs.users);
            
            httpServer.getConfiguration().add(ServerConfiguration.SESSION_TIMEOUT, 60000);
            SunHttpHandler webHandler = new SunHttpHandler(webControllers);
            httpServer.createContext("/", webHandler, filters);

            SunHttpHandler restHandler = new SunHttpHandler(restControllers);
            BasicAuthenticator basicAuthenticator = new RestBasicAuthenticator("test_web_application");
            httpServer.createContext("/users", restHandler, basicAuthenticator);

            httpServer.start();
        });

        When("^I send a (.*) request to web server$", (String page) -> {
            sendGetRequest(page);
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

        Given("^credentials (.*)/(.*)", (String user, String password) -> {
            this.username = user;
            this.password = password;
        });
        When("^I send try to login to web server$", () -> {
            login(username, password);
        });
        And("^I log in with (.*)/(.*) credentials$", (String username, String password) -> {
            login(username, password);
        });
        When("^I logout$", () -> {
            sendGetRequest("/logout");
        });
        Given("^(.*) logs in the system$", (String username) -> {
            Optional<UserData> user = StartServerStepDefs.users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
            if(user.isPresent()){
                login(user.get().getUsername(), user.get().getPassword());
            }
            else{
                throw new RuntimeException(String.format("UserData %s not found", username));
            }
        });
        When("^sends a (.*) request to web server$", (String resource) -> {
            sendGetRequest(resource);
        });

    }

    private void addUsers(List<UserData> users) {
        UserRepository userRepository = InMemoryUserRepository.getInstance();
        users.stream().forEach(userData -> {
            Roles roles = Roles.builder().build();
            Arrays.stream(userData.getRoles()).forEach(rolename -> roles.add(rolename));
            userRepository.persist(User.builder().username(userData.getUsername()).roles(roles).password(userData.getPassword()).build());
        });
    }

    private void addPermissionsToServer(final List<PermissionData> permissionsData, Permissions permissions) {
        permissionsData.forEach(permissionData -> {
            Roles roles = Roles.builder().build();
            Arrays.stream(permissionData.getRoles()).forEach(rolename -> roles.add(rolename));
            permissions.add(Permission.builder().resource(permissionData.getResource()).roles(roles).build());
        });
    }

    private void sendGetRequest(String page) {
        HttpClient httpClient = HttpUtil.create();
        HttpGet httpGet = new HttpGet("http://localhost:" + port + page);
        try {
            httpGet.addHeader(sessionTokenHeader);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            response = getStringFromInputStream(httpResponse.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(String username, String password) {
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
            sessionTokenHeader = httpResponse.getFirstHeader(ResponseHeaderConstants.SET_COOKIE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
