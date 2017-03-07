package cabanas.garcia.ismael.opportunity;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/acceptance-test/resources/features/"}
)
public class CucumberRunner {
}
