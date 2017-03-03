package cabanas.garcia.ismael.opportunity;

import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.server.UnavailableServerException;
import cabanas.garcia.ismael.opportunity.server.WebServer;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

public class StartServerStepDefs implements En {

    private WebServer standardWebServer;

    public StartServerStepDefs() {
        When("^I start the web server on (\\d+) port$", (Integer port) -> {
            standardWebServer = new SunHttpServer(port);
            try {
                standardWebServer.start();
            } catch (UnavailableServerException e) {
                throw new RuntimeException(e);
            }
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