package cabanas.garcia.ismael.opportunity.steps;

import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpAuthenticationFilter;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import com.sun.net.httpserver.Filter;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.*;

public class StartServerStepDefs implements En {

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

            SunHttpAuthenticationFilter authenticationFilter = new SunHttpAuthenticationFilter();

            Arrays.stream(resourcesSplitted).forEach(resource -> authenticationFilter.getConfiguration().addPrivateResource(resource));
            authenticationFilter.getConfiguration().redirectPath("/login");

            filters.add(authenticationFilter);
        });
    }
}