import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "feature",
    glue = "stepDefinitions",
    plugin = {"json:reports/example-report.json"}
)
public class ExampleTest {

}