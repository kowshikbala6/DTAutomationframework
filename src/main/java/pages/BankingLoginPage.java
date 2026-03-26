package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BankingLoginPage extends pages.BasePage {

    private By usernameField = By.name("customer.username");
    private By passwordField = By.name("customer.password");
    private By loginButton = By.xpath("//input[@value='Log In']");
    private By loginForm = By.xpath("//div[@id='loginPanel']");
    private By errorMessage = By.xpath("//span[@class='error' or contains(text(), 'error')]");
    private By forgotPasswordLink = By.xpath("//a[contains(text(), 'Forgot')]");
    private By registerLink = By.xpath("//a[contains(text(), 'Register')]");

    public BankingLoginPage(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Wait for login page to load
     */
    public void waitForLoginPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(loginForm));
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
    }

    /**
     * Perform login with username and password
     */
    public void login(String username, String password) {
        waitForLoginPageToLoad();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement forgotLink = wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
        forgotLink.click();
    }
}
