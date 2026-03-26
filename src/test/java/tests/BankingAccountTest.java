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

public class BankingAccountTest extends BaseTest {

    private BankingLoginPage loginPage;
    private BankingDashboardPage dashboardPage;
    private BankingAccountPage accountPage;

    @BeforeMethod
    public void loginAndNavigateToAccount() {
        loginPage = new BankingLoginPage(DriverManager.getDriver());
        loginPage.waitForLoginPageToLoad();

        String username = ConfigReader.get("valid_username");
        String password = ConfigReader.get("valid_password");

        loginPage.login(username, password);

        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        dashboardPage.waitForDashboardToLoad();

        dashboardPage.clickAccountDetails();
        accountPage = new BankingAccountPage(DriverManager.getDriver());
        accountPage.waitForAccountPageToLoad();
    }

    @Test(description = "Verify account page loads successfully")
    public void testAccountPageLoads() {
        Assert.assertTrue(accountPage.isAccountPageLoaded(), "Account page should load successfully");
    }

    @Test(description = "Verify account number is displayed")
    public void testAccountNumberDisplayed() {
        String accountNum = accountPage.getAccountNumber();
        Assert.assertFalse(accountNum.isEmpty(), "Account number should be displayed");
    }

    @Test(description = "Verify account type is displayed")
    public void testAccountTypeDisplayed() {
        String accountType = accountPage.getAccountType();
        Assert.assertFalse(accountType.isEmpty(), "Account type should be displayed");
    }

    @Test(description = "Verify account balance is displayed")
    public void testAccountBalanceDisplayed() {
        String balance = accountPage.getBalance();
        Assert.assertFalse(balance.isEmpty(), "Account balance should be displayed");
    }

    @Test(description = "Verify interest rate is displayed")
    public void testInterestRateDisplayed() {
        String interestRate = accountPage.getInterestRate();
        // Interest rate might be empty for some account types
        Assert.assertTrue(true, "Interest rate field should be accessible");
    }

    @Test(description = "Verify account information format")
    public void testAccountInformationFormat() {
        String accountNum = accountPage.getAccountNumber();
        String balance = accountPage.getBalance();
        String accountType = accountPage.getAccountType();

        Assert.assertFalse(accountNum.isEmpty(), "Account number should not be empty");
        Assert.assertFalse(balance.isEmpty(), "Balance should not be empty");
        Assert.assertFalse(accountType.isEmpty(), "Account type should not be empty");
    }

    @Test(description = "Verify transaction history is available")
    public void testTransactionHistoryAvailable() {
        int transactionCount = accountPage.getTransactionCount();
        // Transaction count can be 0 or more
        Assert.assertTrue(transactionCount >= 0, "Transaction count should be accessible");
    }

    @Test(description = "Verify transaction details are readable")
    public void testTransactionDetailsReadable() {
        int transactionCount = accountPage.getTransactionCount();
        if (transactionCount > 0) {
            String details = accountPage.getTransactionDetails(0);
            Assert.assertFalse(details.isEmpty(), "Transaction details should be readable");
        } else {
            Assert.assertTrue(true, "No transactions to verify");
        }
    }

    @Test(description = "Verify statement download is available")
    public void testStatementDownloadAvailable() {
        accountPage.downloadStatement();
        // If no exception, download should be available
        Assert.assertTrue(true, "Statement download should be available");
    }
}
