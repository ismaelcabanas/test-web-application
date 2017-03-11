package cabanas.garcia.ismael.opportunity.steps;

import cucumber.api.PendingException;
import cucumber.api.java8.En;

/**
 * Created by XI317311 on 10/03/2017.
 */
public class ApiUsersStepDefs implements En {
    public ApiUsersStepDefs() {
        Given("^I want create user with name (.*)", (String username) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        And("^with password (.*)$", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        And("^with roles (.*)$", (Integer arg0) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        When("^I use API for creating users$", () -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
        Then("^the web server returns (\\d+) status code$", (Integer statusCode) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
    }
}
