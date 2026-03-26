package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends pages.BasePage {

    // Prefer stable locators for the login form fields
    By email = By.id("email");
    By password = By.id("pass");
    By loginBtn = By.id("send2");

    public void openLogin() {
        // Navigate directly to the known login page path to avoid fragile home-page link text
        String loginUrl = "";
        try {
            // baseURL is configured via ConfigReader -> BaseTest opens baseURL already, so build on that
            String base = System.getProperty("baseURL");
            if (base == null || base.isEmpty()) {
                // Fallback to using the driver current URL's origin
                String current = driver.getCurrentUrl();
                if (current.endsWith("/")) {
                    current = current.substring(0, current.length() - 1);
                }
                loginUrl = current + "/customer/account/login/";
            } else {
                if (base.endsWith("/")) {
                    base = base.substring(0, base.length() - 1);
                }
                loginUrl = base + "/customer/account/login/";
            }
        } catch (Exception e) {
            // As a safe fallback
            loginUrl = "https://magento.softwaretestingboard.com/customer/account/login/";
        }

        driver.get(loginUrl);

        // Wait for the email field to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(email));
    }

    public void login(String user, String pass) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement emailEl = wait.until(ExpectedConditions.elementToBeClickable(email));
        emailEl.clear();
        emailEl.sendKeys(user);

        WebElement passEl = wait.until(ExpectedConditions.elementToBeClickable(password));
        passEl.clear();
        passEl.sendKeys(pass);

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        btn.click();

    }

}