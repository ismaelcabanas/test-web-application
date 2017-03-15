package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.repository.InMemorySessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthorizationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.steps.model.PermissionData;
import cabanas.garcia.ismael.opportunity.steps.model.UserData;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.filters;
import static cabanas.garcia.ismael.opportunity.steps.Hooks.httpServer;

public class StartServerStepDefs implements En {

    public static List<PermissionData> permissions = new ArrayList<>();
    public static List<UserData> users = new ArrayList<>();

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

            SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                    new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();

            Arrays.stream(resourcesSplitted).forEach(resource -> configuration.addPrivateResource(resource.trim()));
            configuration.redirectPath("/login");

            SunHttpAuthorizationFilter authenticationFilter = new SunHttpAuthorizationFilter(configuration);

            filters.add(authenticationFilter);
        });

        And("^there are the next users in the system$", (DataTable dataTable) -> {
            this.users = dataTable.asList(UserData.class);
        });
        Given("^the next table of permissions$", (DataTable dataTable) -> {
            this.permissions = dataTable.asList(PermissionData.class);
        });
    }
}