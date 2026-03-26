package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static void initDriver(String browser) {

        WebDriver driver = null;

        if(browser.equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            // Allow remote origins for newer ChromeDriver/Chrome combinations
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--no-first-run");
            options.addArguments("--no-default-browser-check");
            // options.addArguments("--headless=new"); // enable if headless required

            // Reduce automation flags
            options.setExperimentalOption("useAutomationExtension", false);
            Map<String, Object> exclude = new HashMap<>();
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

            options.setAcceptInsecureCerts(true);

            driver = new ChromeDriver(options);

        }

        driver.manage().window().maximize();
        // add a small implicit wait to reduce timing issues
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        DriverManager.setDriver(driver);

    }

}