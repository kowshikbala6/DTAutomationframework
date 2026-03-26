package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BankingAccountPage extends pages.BasePage {

    private By accountTitle = By.xpath("//h2[contains(text(), 'Account')]");
    private By accountNumberField = By.xpath("//div[contains(text(), 'Account Number')]/following-sibling::div | //span[contains(text(), 'Account Number')]/following-sibling::span");
    private By accountTypeField = By.xpath("//div[contains(text(), 'Account Type')]/following-sibling::div | //span[contains(text(), 'Account Type')]/following-sibling::span");
    private By balanceField = By.xpath("//div[contains(text(), 'Balance')]/following-sibling::div | //span[contains(text(), 'Balance')]/following-sibling::span");
    private By interestRateField = By.xpath("//div[contains(text(), 'Interest')]/following-sibling::div | //span[contains(text(), 'Interest')]/following-sibling::span");
    private By editAccountButton = By.xpath("//button[contains(text(), 'Edit') or contains(text(), 'Update')]");
    private By transactionTable = By.xpath("//table | //div[@role='table']");
    private By transactionRows = By.xpath("//table//tbody//tr | //div[@role='row']");
    private By downloadStatement = By.xpath("//button[contains(text(), 'Download') or contains(text(), 'Statement')]");
    private By accountClosureButton = By.xpath("//button[contains(text(), 'Close') or contains(text(), 'Closure')]");

    public BankingAccountPage(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Wait for account page to load
     */
    public void waitForAccountPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(accountTitle));
    }

    /**
     * Check if account page is loaded
     */
    public boolean isAccountPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get account number
     */
    public String getAccountNumber() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement accountNum = wait.until(ExpectedConditions.visibilityOfElementLocated(accountNumberField));
            return accountNum.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get account type
     */
    public String getAccountType() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement accountType = wait.until(ExpectedConditions.visibilityOfElementLocated(accountTypeField));
            return accountType.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get account balance
     */
    public String getBalance() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement balance = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceField));
            return balance.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get interest rate
     */
    public String getInterestRate() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement interest = wait.until(ExpectedConditions.visibilityOfElementLocated(interestRateField));
            return interest.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get transaction count
     */
    public int getTransactionCount() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(transactionRows));
            List<WebElement> rows = driver.findElements(transactionRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Click edit account
     */
    public void clickEditAccount() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(editAccountButton));
            editBtn.click();
        } catch (Exception e) {
            // Edit button may not be available
        }
    }

    /**
     * Download statement
     */
    public void downloadStatement() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(downloadStatement));
            downloadBtn.click();
        } catch (Exception e) {
            // Download button may not be available
        }
    }

    /**
     * Get transaction details
     */
    public String getTransactionDetails(int rowNumber) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(transactionRows));
            List<WebElement> rows = driver.findElements(transactionRows);
            if (rowNumber < rows.size()) {
                return rows.get(rowNumber).getText();
            }
        } catch (Exception e) {
            // No transactions
        }
        return "";
    }
}
