package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BankingDashboardPage extends pages.BasePage {

    private By welcomeMessage = By.xpath("//h2[contains(text(), 'Dashboard') or contains(text(), 'Welcome')]");
    private By accountSection = By.xpath("//div[@role='tabpanel' or @class='account-section']");
    private By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");
    private By profileIcon = By.xpath("//button[@aria-label='Toggle user menu' or contains(@class, 'profile')]");
    private By accountDetailsButton = By.xpath("//button[contains(text(), 'Account') or contains(text(), 'Details')]");
    private By transferButton = By.xpath("//button[contains(text(), 'Transfer') or contains(text(), 'Money')]");
    private By depositsButton = By.xpath("//button[contains(text(), 'Deposits') or contains(text(), 'Deposit')]");
    private By withdrawButton = By.xpath("//button[contains(text(), 'Withdraw') or contains(text(), 'Withdrawal')]");
    private By transactionHistoryButton = By.xpath("//button[contains(text(), 'Transactions') or contains(text(), 'History')]");

    public BankingDashboardPage(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Wait for dashboard to load
     */
    public void waitForDashboardToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(welcomeMessage));
    }

    /**
     * Check if dashboard is loaded
     */
    public boolean isDashboardLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get welcome message
     */
    public String getWelcomeMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage));
        return message.getText();
    }

    /**
     * Click on account details
     */
    public void clickAccountDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement accountBtn = wait.until(ExpectedConditions.elementToBeClickable(accountDetailsButton));
        accountBtn.click();
    }

    /**
     * Click transfer button
     */
    public void clickTransfer() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement transferBtn = wait.until(ExpectedConditions.elementToBeClickable(transferButton));
        transferBtn.click();
    }

    /**
     * Click deposits button
     */
    public void clickDeposits() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement depositBtn = wait.until(ExpectedConditions.elementToBeClickable(depositsButton));
        depositBtn.click();
    }

    /**
     * Click withdraw button
     */
    public void clickWithdraw() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement withdrawBtn = wait.until(ExpectedConditions.elementToBeClickable(withdrawButton));
        withdrawBtn.click();
    }

    /**
     * Click transaction history button
     */
    public void clickTransactionHistory() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement historyBtn = wait.until(ExpectedConditions.elementToBeClickable(transactionHistoryButton));
        historyBtn.click();
    }

    /**
     * Logout from dashboard
     */
    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Try to find logout button directly
        try {
            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
            logout.click();
        } catch (Exception e) {
            // If direct logout button not found, try clicking profile icon first
            try {
                WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileIcon));
                profile.click();
                WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
                logout.click();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to logout", ex);
            }
        }
    }

    /**
     * Get account balance
     */
    public String getAccountBalance() {
        try {
            By balanceLocator = By.xpath("//div[contains(text(), 'Balance') or contains(text(), 'balance')]/following-sibling::div | //span[contains(@class, 'balance')]");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement balanceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceLocator));
            return balanceElement.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
