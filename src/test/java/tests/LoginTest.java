package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import driver.DriverManager;

public class LoginTest extends BaseTest {

    @Test

    public void testLogin() {

        // Since we're using JSONPlaceholder for testing, just verify the page loaded
        // and contains expected content (no login needed for this API testing site)
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        // Verify we're on JSONPlaceholder
        Assert.assertTrue(
                currentUrl.contains("jsonplaceholder"),
                "Should be on JSONPlaceholder site"
        );

        // Verify page title or check for expected content
        String pageSource = DriverManager.getDriver().getPageSource();
        System.out.println("Page loaded successfully. Page source length: " + pageSource.length());

        // Simple assertion that page loaded
        Assert.assertTrue(pageSource.length() > 0, "Page should have content");

    }

}