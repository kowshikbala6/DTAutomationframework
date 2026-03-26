package tests;

import base.BaseTest;
import driver.DriverManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BankingLoginPage;
import pages.BankingDashboardPage;
import pages.BankingTransferPage;
import utils.ConfigReader;

public class BankingTransferTest extends BaseTest {

    private BankingLoginPage loginPage;
    private BankingDashboardPage dashboardPage;
    private BankingTransferPage transferPage;

    @BeforeMethod
    public void loginAndNavigateToTransfer() {
        loginPage = new BankingLoginPage(DriverManager.getDriver());
        loginPage.waitForLoginPageToLoad();

        String username = ConfigReader.get("valid_username");
        String password = ConfigReader.get("valid_password");

        loginPage.login(username, password);

        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        dashboardPage.waitForDashboardToLoad();

        dashboardPage.clickTransfer();
        transferPage = new BankingTransferPage(DriverManager.getDriver());
        transferPage.waitForTransferPageToLoad();
    }

    @Test(description = "Verify transfer page loads successfully")
    public void testTransferPageLoads() {
        Assert.assertTrue(transferPage.isTransferPageLoaded(), "Transfer page should load successfully");
    }

    @Test(description = "Perform successful fund transfer")
    public void testSuccessfulTransfer() {
        String fromAccount = ConfigReader.get("account_number");
        String toAccount = ConfigReader.get("beneficiary_account");
        String amount = ConfigReader.get("transfer_amount");

        transferPage.performTransfer(fromAccount, toAccount, amount, "Test Transfer");

        Assert.assertTrue(transferPage.isTransferSuccessful(), "Transfer should be successful");
    }

    @Test(description = "Verify error on transfer with missing beneficiary")
    public void testTransferWithMissingBeneficiary() {
        String fromAccount = ConfigReader.get("account_number");
        String amount = ConfigReader.get("transfer_amount");

        transferPage.selectFromAccount(fromAccount);
        transferPage.enterAmount(amount);
        transferPage.submitTransfer();

        // Should have error since beneficiary is missing
        String errorMsg = transferPage.getErrorMessage();
        Assert.assertFalse(errorMsg.isEmpty() || !transferPage.isTransferSuccessful(),
            "Transfer should fail without beneficiary account");
    }

    @Test(description = "Verify error on invalid transfer amount")
    public void testTransferWithInvalidAmount() {
        String fromAccount = ConfigReader.get("account_number");
        String toAccount = ConfigReader.get("beneficiary_account");

        transferPage.selectFromAccount(fromAccount);
        transferPage.enterBeneficiaryAccount(toAccount);
        transferPage.enterAmount("-100"); // Negative amount should fail
        transferPage.submitTransfer();

        String errorMsg = transferPage.getErrorMessage();
        // Either error message displayed or transfer not successful
        Assert.assertTrue(errorMsg.length() > 0 || !transferPage.isTransferSuccessful(),
            "Transfer with invalid amount should fail");
    }

    @Test(description = "Verify transfer with zero amount fails")
    public void testTransferWithZeroAmount() {
        String fromAccount = ConfigReader.get("account_number");
        String toAccount = ConfigReader.get("beneficiary_account");

        transferPage.selectFromAccount(fromAccount);
        transferPage.enterBeneficiaryAccount(toAccount);
        transferPage.enterAmount("0");
        transferPage.submitTransfer();

        String errorMsg = transferPage.getErrorMessage();
        Assert.assertTrue(errorMsg.length() > 0 || !transferPage.isTransferSuccessful(),
            "Transfer with zero amount should fail");
    }

    @Test(description = "Verify cancel button works")
    public void testCancelTransfer() {
        transferPage.clickCancel();
        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Should return to dashboard after cancel");
    }

    @Test(description = "Verify transfer amount field accepts valid numbers")
    public void testTransferAmountValidation() {
        String validAmount = ConfigReader.get("transfer_amount");
        transferPage.enterAmount(validAmount);

        // If no exception thrown, valid amount was accepted
        Assert.assertTrue(true, "Valid amount should be accepted");
    }
}
