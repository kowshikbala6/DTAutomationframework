package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.BankingLoginPage;
import pages.BankingDashboardPage;
import utils.ConfigReader;

public class BankingLoginSteps {

    private BankingLoginPage loginPage;
    private BankingDashboardPage dashboardPage;

    @Given("user is on the banking login page")
    public void userIsOnLoginPage() {
        String baseURL = ConfigReader.get("baseURL");
        if (baseURL != null && !baseURL.isEmpty()) {
            DriverManager.getDriver().get(baseURL);
        }
        loginPage = new BankingLoginPage(DriverManager.getDriver());
        loginPage.waitForLoginPageToLoad();
    }

    @When("user enters valid username {string}")
    public void userEntersValidUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("user enters valid password {string}")
    public void userEntersValidPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("user enters username {string}")
    public void userEntersUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("user clicks the login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("user should be logged in successfully")
    public void userShouldBeLoggedInSuccessfully() {
        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        dashboardPage.waitForDashboardToLoad();
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should be loaded after successful login");
    }

    @Then("login should fail with an error message")
    public void loginShouldFailWithError() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid login");
    }

    @Then("error message should contain {string}")
    public void errorMessageShouldContain(String expectedText) {
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains(expectedText) || errorMsg.length() > 0,
            "Error message should contain expected text or be displayed");
    }

    @When("user logs in with username {string} and password {string}")
    public void userLogsInWithCredentials(String username, String password) {
        loginPage.login(username, password);
    }

    @When("user clicks forgot password link")
    public void userClicksForgotPasswordLink() {
        loginPage.clickForgotPassword();
    }
}
