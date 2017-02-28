package cabanas.garcia.ismael.opportunity;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

public class StartServerStepDefs implements En {

    private WebServer webServer;

    public StartServerStepDefs() {
        When("^I start the web server$", () -> {
            webServer = new WebServer();
            webServer.start();
        });
        Then("^the web server is up$", () -> {
            Assert.assertThat(webServer.isUp(), Is.is(IsEqual.equalTo(true)));
        });
    }
}
