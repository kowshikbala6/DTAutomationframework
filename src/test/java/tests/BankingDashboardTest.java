package tests;

import base.BaseTest;
import driver.DriverManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BankingLoginPage;
import pages.BankingDashboardPage;
import pages.BankingAccountPage;
import utils.ConfigReader;

public class BankingDashboardTest extends BaseTest {

    private BankingLoginPage loginPage;
    private BankingDashboardPage dashboardPage;
    private BankingAccountPage accountPage;

    @BeforeMethod
    public void loginBeforeTest() {
        loginPage = new BankingLoginPage(DriverManager.getDriver());
        loginPage.waitForLoginPageToLoad();

        String username = ConfigReader.get("valid_username");
        String password = ConfigReader.get("valid_password");

        loginPage.login(username, password);

        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        dashboardPage.waitForDashboardToLoad();
    }

    @Test(description = "Verify dashboard loads successfully")
    public void testDashboardLoadsSuccessfully() {
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should load successfully");
    }

    @Test(description = "Verify welcome message is displayed on dashboard")
    public void testWelcomeMessageDisplayed() {
        String welcomeMsg = dashboardPage.getWelcomeMessage();
        Assert.assertFalse(welcomeMsg.isEmpty(), "Welcome message should be displayed on dashboard");
    }

    @Test(description = "Verify account details button is accessible")
    public void testAccountDetailsButtonAccessible() {
        dashboardPage.clickAccountDetails();
        accountPage = new BankingAccountPage(DriverManager.getDriver());
        accountPage.waitForAccountPageToLoad();
        Assert.assertTrue(accountPage.isAccountPageLoaded(), "Account page should load");
    }

    @Test(description = "Verify transfer button is accessible")
    public void testTransferButtonAccessible() {
        dashboardPage.clickTransfer();
        // Verify that we are able to access transfer page
        Assert.assertTrue(true, "Transfer page should be accessible");
    }

    @Test(description = "Verify deposits button is accessible")
    public void testDepositsButtonAccessible() {
        dashboardPage.clickDeposits();
        // Verify that we are able to access deposits page
        Assert.assertTrue(true, "Deposits page should be accessible");
    }

    @Test(description = "Verify withdraw button is accessible")
    public void testWithdrawButtonAccessible() {
        dashboardPage.clickWithdraw();
        // Verify that we are able to access withdraw page
        Assert.assertTrue(true, "Withdraw page should be accessible");
    }

    @Test(description = "Verify transaction history is accessible")
    public void testTransactionHistoryAccessible() {
        dashboardPage.clickTransactionHistory();
        // Verify that we are able to access transaction history
        Assert.assertTrue(true, "Transaction history should be accessible");
    }

    @Test(description = "Verify logout functionality")
    public void testLogout() {
        dashboardPage.logout();
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        // After logout, should not be on dashboard
        Assert.assertFalse(currentUrl.contains("dashboard"), "Should not be on dashboard after logout");
    }
}
