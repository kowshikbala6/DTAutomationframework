package stepdefinitions;

import driver.DriverManager;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.BankingTransferPage;
import pages.BankingDashboardPage;

public class BankingTransferSteps {

    private BankingTransferPage transferPage;
    private BankingDashboardPage dashboardPage;

    @Given("user is on the transfer page")
    public void userIsOnTransferPage() {
        transferPage = new BankingTransferPage(DriverManager.getDriver());
        transferPage.waitForTransferPageToLoad();
    }

    @When("user selects from account {string}")
    public void userSelectsFromAccount(String account) {
        transferPage.selectFromAccount(account);
    }

    @When("user enters beneficiary account {string}")
    public void userEntersBeneficiaryAccount(String account) {
        transferPage.enterBeneficiaryAccount(account);
    }

    @When("user enters transfer amount {string}")
    public void userEntersTransferAmount(String amount) {
        transferPage.enterAmount(amount);
    }

    @When("user enters description {string}")
    public void userEntersDescription(String description) {
        transferPage.enterDescription(description);
    }

    @When("user submits the transfer")
    public void userSubmitsTransfer() {
        transferPage.submitTransfer();
    }

    @Then("transfer should be successful")
    public void transferShouldBeSuccessful() {
        Assert.assertTrue(transferPage.isTransferSuccessful(), "Transfer should be successful");
    }

    @Then("success message should be displayed")
    public void successMessageShouldBeDisplayed() {
        String successMsg = transferPage.getSuccessMessage();
        Assert.assertFalse(successMsg.isEmpty(), "Success message should be displayed");
    }

    @When("user performs transfer from {string} to {string} with amount {string}")
    public void userPerformsTransfer(String fromAccount, String toAccount, String amount) {
        transferPage.performTransfer(fromAccount, toAccount, amount, "Test Transfer");
    }

    @Then("transfer should fail with error message")
    public void transferShouldFailWithError() {
        String errorMsg = transferPage.getErrorMessage();
        Assert.assertFalse(errorMsg.isEmpty(), "Error message should be displayed for failed transfer");
    }

    @When("user clicks cancel on transfer")
    public void userClicksCancelOnTransfer() {
        transferPage.clickCancel();
    }

    @Then("transfer page should be closed")
    public void transferPageShouldBeClosed() {
        dashboardPage = new BankingDashboardPage(DriverManager.getDriver());
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Should be back on dashboard after cancel");
    }
}
