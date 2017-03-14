package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthenticationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.steps.model.User;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.filters;
import static cabanas.garcia.ismael.opportunity.steps.Hooks.httpServer;

public class StartServerStepDefs implements En {

    public static List<Permission> permissions;
    public static List<User> users;

    public StartServerStepDefs() {
        When("^I start the web server on (\\d+) port$", (Integer port) -> {
            httpServer = new SunHttpServer(port);
            httpServer.start();
        });
        Then("^the web server is up$", () -> {
            Assert.assertThat(httpServer.isRunning(), Is.is(IsEqual.equalTo(true)));
        });

        And("^I stopped it$", () -> {
            httpServer.stop();
        });
        Then("^the web server is down$", () -> {
            Assert.assertThat(httpServer.isRunning(), Is.is(IsEqual.equalTo(false)));
        });
        Given("^private resources (.*)$", (String resources) -> {
            String[] resourcesSplitted = resources.split(",");

            SunHttpAuthenticationFilter authenticationFilter = new SunHttpAuthenticationFilter(InMemorySessionRepository.getInstance());

            Arrays.stream(resourcesSplitted).forEach(resource -> authenticationFilter.getConfiguration().addPrivateResource(resource));
            authenticationFilter.getConfiguration().redirectPath("/login");

            filters.add(authenticationFilter);
        });
        Given("^the next table of permissions$", (DataTable dataTable) -> {
            this.permissions = dataTable.asList(Permission.class);
        });
        And("^there are the next users in the system$", (DataTable dataTable) -> {
            this.users = dataTable.asList(User.class);
        });
    }
}