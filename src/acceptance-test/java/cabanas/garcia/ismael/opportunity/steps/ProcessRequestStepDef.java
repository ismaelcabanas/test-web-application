package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.http.RequestHeadersEnum;
import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import cabanas.garcia.ismael.opportunity.http.session.DefaultSessionManager;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.security.permission.DefaultPermissionChecker;
import cabanas.garcia.ismael.opportunity.security.permission.DefaultRestPermissionChecker;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.server.authenticators.RestBasicAuthenticator;
import cabanas.garcia.ismael.opportunity.server.sun.ServerConfiguration;
import cabanas.garcia.ismael.opportunity.server.sun.filter.SunHttpAuthorizationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.handlers.SunHttpHandler;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.server.sun.handlers.RestHandler;
import cabanas.garcia.ismael.opportunity.service.DefaultUserService;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.steps.model.Response;
import cabanas.garcia.ismael.opportunity.steps.util.HttpUtil;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpHandler;
import cucumber.api.java8.En;
import org.apache.http.Header;

import java.net.HttpURLConnection;
import java.util.Collections;
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
    Response httpResponse;

    public ProcessRequestStepDef() {

        Given("^the web server is running on port (\\d+)$", (Integer port) -> {
            this.port = port;
            httpServer = new SunHttpServer(port);

            // set authorization filter
            SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                    new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
            configuration.redirectPath("/login");
            configuration.redirectForbiddenPath("/forbidden");

            SessionManager sessionManager = new DefaultSessionManager(InMemorySessionRepository.getInstance());

            PermissionChecker permissionChecker = new DefaultPermissionChecker(StartServerStepDefs.permissions);

            SunHttpAuthorizationFilter authenticationFilter =
                    new SunHttpAuthorizationFilter(configuration, sessionManager,
                            permissionChecker,
                            StartServerStepDefs.privateResourceService);
            filters.add(authenticationFilter);

            httpServer.getConfiguration().add(ServerConfiguration.SESSION_TIMEOUT, 60);
            httpServer.getConfiguration().add(ServerConfiguration.REDIRECT_LOGOUT, "/login");
            HttpHandler webHandler = new SunHttpHandler(webControllers);
            httpServer.createContext("/", webHandler, filters);

            UserService userService = new DefaultUserService(InMemoryUserRepository.getInstance());
            PermissionChecker permissionCheckerForRestContext = new DefaultRestPermissionChecker();
            HttpHandler restHandler = new RestHandler(restControllers, userService, permissionCheckerForRestContext);

            UserService userServiceForAuthenticator = new DefaultUserService(InMemoryUserRepository.getInstance());
            BasicAuthenticator basicAuthenticator = new RestBasicAuthenticator("test_web_application", userServiceForAuthenticator);
            httpServer.createContext("/users", restHandler, Collections.EMPTY_LIST, Optional.of(basicAuthenticator));

            httpServer.start();
        });

        When("^I send a (.*) request to web server$", (String page) -> {
            sendGetRequest(page);
        });

        Then("^the web server returns (.*) resource$", (String expected) -> {
            assertThat(httpResponse.getContent(), containsString(expected));
        });

        And("^(\\d+) status code$", (Integer statusCode) -> {
            assertThat(httpResponse.getStatusCode(), is(equalTo(statusCode)));
        });

        Then("^the web server redirects me to login page$", () -> {
            assertThat(this.statusCode, is(equalTo(HttpURLConnection.HTTP_OK)));
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
            Optional<User> user = InMemoryUserRepository.getInstance().read(username);
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


    private void sendGetRequest(String page) {
        String getUri = "http://localhost:" + port + "/" + page;
        HttpUtil httpClientUtil = new HttpUtil(getUri);

        if(sessionTokenHeader != null){
            httpClientUtil.addHeader(RequestHeadersEnum.COOKIE.getName(), sessionTokenHeader.getValue());
        }

        try {
            httpResponse = httpClientUtil.sendGet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isRedirect(int statusCode){
        return statusCode >= 300 && statusCode <= 305 && statusCode != 304;
    }

    private void login(String username, String password) {
        HttpUtil httpClientUtil = new HttpUtil("http://localhost:" + port + "/login");

        httpClientUtil.addFormParameter("username", username);
        httpClientUtil.addFormParameter("password", password);

        try {
            httpResponse = httpClientUtil.sendPost();
            sessionTokenHeader = httpResponse.getHeader(ResponseHeaderConstants.SET_COOKIE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
