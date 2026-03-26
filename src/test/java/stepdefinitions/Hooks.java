package stepdefinitions;

import driver.DriverFactory;
import driver.DriverManager;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;

public class Hooks {

    @Before
    public void setup() {
        String browser = ConfigReader.get("browser");
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }
        DriverFactory.initDriver(browser);
        String baseURL = ConfigReader.get("baseURL");
        if (baseURL != null && !baseURL.isEmpty()) {
            DriverManager.getDriver().get(baseURL);
        }
    }

    @After
    public void teardown() {
        DriverManager.quit();
    }
}
