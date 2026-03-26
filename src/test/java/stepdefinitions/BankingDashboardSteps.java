package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.BankingDashboardPage;
import pages.BankingAccountPage;
import pages.BankingTransferPage;

public class BankingDashboardSteps {

    private BankingDashboardPage dashboardPage;
    private BankingAccountPage accountPage;
    private BankingTransferPage transferPage;

    @Given("user is on the banking dashboard")
    public void userIsOnDashboard() {
        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        dashboardPage.waitForDashboardToLoad();
    }

    @When("user clicks on account details")
    public void userClicksAccountDetails() {
        dashboardPage.clickAccountDetails();
    }

    @Then("account details page should be displayed")
    public void accountDetailsPageShouldBeDisplayed() {
        accountPage = new BankingAccountPage(DriverManager.getDriver());
        accountPage.waitForAccountPageToLoad();
        Assert.assertTrue(accountPage.isAccountPageLoaded(), "Account page should be loaded");
    }

    @Then("account number should be visible")
    public void accountNumberShouldBeVisible() {
        String accountNum = accountPage.getAccountNumber();
        Assert.assertFalse(accountNum.isEmpty(), "Account number should be visible");
    }

    @Then("account balance should be displayed")
    public void accountBalanceShouldBeDisplayed() {
        String balance = accountPage.getBalance();
        Assert.assertFalse(balance.isEmpty(), "Account balance should be displayed");
    }

    @When("user clicks on transfer money")
    public void userClicksTransfer() {
        dashboardPage.clickTransfer();
    }

    @Then("transfer page should be displayed")
    public void transferPageShouldBeDisplayed() {
        transferPage = new BankingTransferPage(DriverManager.getDriver());
        transferPage.waitForTransferPageToLoad();
        Assert.assertTrue(transferPage.isTransferPageLoaded(), "Transfer page should be loaded");
    }

    @When("user clicks on deposits")
    public void userClicksDeposits() {
        dashboardPage.clickDeposits();
    }

    @When("user clicks on withdraw")
    public void userClicksWithdraw() {
        dashboardPage.clickWithdraw();
    }

    @When("user clicks on transaction history")
    public void userClicksTransactionHistory() {
        dashboardPage.clickTransactionHistory();
    }

    @When("user clicks logout")
    public void userClicksLogout() {
        dashboardPage.logout();
    }

    @Then("user should be logged out successfully")
    public void userShouldBeLoggedOutSuccessfully() {
        // Verify that we're back on login page or welcome page
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertFalse(currentUrl.contains("dashboard"), "Should not be on dashboard after logout");
    }
}
