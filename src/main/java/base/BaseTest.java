package base;

import driver.DriverFactory;
import driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

public class BaseTest {

    @BeforeMethod

    public void setup() {

        DriverFactory.initDriver(
                ConfigReader.get("browser")
        );

        DriverManager.getDriver().get(
                ConfigReader.get("baseURL")
        );

    }

    @AfterMethod

    public void teardown() {

        DriverManager.quit();

    }

}