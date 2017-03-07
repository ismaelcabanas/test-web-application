package cabanas.garcia.ismael.opportunity;

import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

public class StartServerStepDefs implements En {

    private SunHttpServer httpServer;
    private StandardWebServer standardWebServer;
    private ControllerScanner controllerScanner;
    private ControllerMapper controllerMapper;

    public StartServerStepDefs() {
        Before(() -> {
            controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller");
            controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
            httpServer = new SunHttpServer();
        });

        When("^I start the web server on (\\d+) port$", (Integer port) -> {
            standardWebServer = new StandardWebServer(port, controllerScanner, controllerMapper, new SunHttpServer());
            standardWebServer.start();
        });
        Then("^the web server is up$", () -> {
            Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(true)));
        });

        After(() -> {
            standardWebServer.stop();
        });
        And("^I stopped it$", () -> {
            standardWebServer.stop();
        });
        Then("^the web server is down$", () -> {
            Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
        });
    }
}