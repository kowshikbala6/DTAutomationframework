package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static String screenshotPath = ConfigReader.get("screenshot_path");

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Ensure directory exists
            File dir = new File(screenshotPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = testName + "_" + timestamp + ".png";
            String filePath = screenshotPath + filename;

            // Take screenshot
            org.openqa.selenium.TakesScreenshot screenshot = (org.openqa.selenium.TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            File destFile = new File(filePath);
            FileHandler.copy(srcFile, destFile);

            System.out.println("Screenshot captured: " + filePath);
            return filePath;
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return "";
        }
    }

    /**
     * Alias method for captureScreenshot - convenience method
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        return captureScreenshot(driver, testName);
    }
}
