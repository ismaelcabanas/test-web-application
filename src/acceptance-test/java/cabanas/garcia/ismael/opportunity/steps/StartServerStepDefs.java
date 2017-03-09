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
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

import static cabanas.garcia.ismael.opportunity.steps.Hooks.*;

public class StartServerStepDefs implements En {

    public StartServerStepDefs() {
        When("^I start the web server on (\\d+) port$", (Integer port) -> {
            standardWebServer = new StandardWebServer(port, controllerScanner, controllerMapper, new SunHttpServer());
            standardWebServer.start();
        });
        Then("^the web server is up$", () -> {
            Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(true)));
        });

        And("^I stopped it$", () -> {
            standardWebServer.stop();
        });
        Then("^the web server is down$", () -> {
            Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
        });
        Given("^private resource (.*)$", (String resource) -> {
            // TODO Configure private resources to server here
        });
    }
}