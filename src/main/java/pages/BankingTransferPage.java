package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BankingTransferPage extends pages.BasePage {

    private By transferTitle = By.xpath("//h2[contains(text(), 'Transfer') or contains(text(), 'Money')]");
    private By fromAccountDropdown = By.xpath("//select[@id='fromAccount' or @name='fromAccount'] | //div[contains(text(), 'From Account')]/following-sibling::div//button");
    private By toAccountField = By.xpath("//input[@id='toAccount' or @name='toAccount' or @placeholder='Beneficiary Account']");
    private By amountField = By.xpath("//input[@id='amount' or @name='amount' or @placeholder='Amount']");
    private By descriptionField = By.xpath("//textarea[@id='description' or @name='description' or @placeholder='Description']");
    private By submitButton = By.xpath("//button[@type='submit'][contains(text(), 'Transfer') or contains(text(), 'Send')]");
    private By successMessage = By.xpath("//div[@role='alert' and contains(@class, 'success')] | //p[contains(text(), 'successful')]");
    private By errorMessage = By.xpath("//div[@role='alert' and contains(@class, 'error')] | //p[contains(text(), 'error')]");
    private By cancelButton = By.xpath("//button[contains(text(), 'Cancel')]");

    public BankingTransferPage(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Wait for transfer page to load
     */
    public void waitForTransferPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(transferTitle));
    }

    /**
     * Check if transfer page is loaded
     */
    public boolean isTransferPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(transferTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Select from account
     */
    public void selectFromAccount(String accountNumber) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(fromAccountDropdown));
        dropdown.click();

        By optionLocator = By.xpath("//option[contains(text(), '" + accountNumber + "')] | //div[contains(text(), '" + accountNumber + "')]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
    }

    /**
     * Enter beneficiary account
     */
    public void enterBeneficiaryAccount(String account) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement accountField = wait.until(ExpectedConditions.elementToBeClickable(toAccountField));
        accountField.clear();
        accountField.sendKeys(account);
    }

    /**
     * Enter transfer amount
     */
    public void enterAmount(String amount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement amountElement = wait.until(ExpectedConditions.elementToBeClickable(amountField));
        amountElement.clear();
        amountElement.sendKeys(amount);
    }

    /**
     * Enter description
     */
    public void enterDescription(String description) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement descElement = wait.until(ExpectedConditions.elementToBeClickable(descriptionField));
            descElement.clear();
            descElement.sendKeys(description);
        } catch (Exception e) {
            // Description field may be optional
        }
    }

    /**
     * Submit transfer
     */
    public void submitTransfer() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitBtn.click();
    }

    /**
     * Perform complete transfer
     */
    public void performTransfer(String fromAccount, String toAccount, String amount, String description) {
        waitForTransferPageToLoad();
        selectFromAccount(fromAccount);
        enterBeneficiaryAccount(toAccount);
        enterAmount(amount);
        enterDescription(description);
        submitTransfer();
    }

    /**
     * Get success message
     */
    public String getSuccessMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return success.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if transfer was successful
     */
    public boolean isTransferSuccessful() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return success.isDisplayed();
        } catch (Exception e) {
            return false;
        }
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
     * Click cancel button
     */
    public void clickCancel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cancel = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancel.click();
    }
}
