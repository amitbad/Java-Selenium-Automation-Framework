package com.automation.base;

import com.automation.config.ConfigManager;
import com.automation.driver.DriverManager;
import com.automation.listeners.TestListener;
import com.automation.reports.ExtentReportManager;
import com.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;

/**
 * Base Test - Abstract class providing common test setup and teardown.
 * All test classes should extend this class.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Get WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
    
    /**
     * Suite setup - runs once before all tests in the suite
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========== TEST SUITE STARTED ==========");
        ExtentReportManager.initReports();
    }
    
    /**
     * Test setup - runs before each test class
     */
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        logger.info("Starting test class: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Method setup - runs before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        logger.info("Initializing WebDriver for test");
        DriverManager.initDriver();
        
        // Navigate to base URL if configured
        String baseUrl = config.getBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty() && !baseUrl.contains("${")) {
            getDriver().get(baseUrl);
            logger.info("Navigated to base URL: {}", baseUrl);
        }
    }
    
    /**
     * Method teardown - runs after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        // Take screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test FAILED: {}", result.getName());
            
            if (config.shouldTakeScreenshotOnFailure()) {
                try {
                    byte[] screenshot = ScreenshotUtils.takeScreenshot(getDriver());
                    String screenshotPath = ScreenshotUtils.saveScreenshot(screenshot, result.getName());
                    
                    // Attach to Allure report
                    Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
                    
                    logger.info("Screenshot saved: {}", screenshotPath);
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot", e);
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: {}", result.getName());
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Test SKIPPED: {}", result.getName());
        }
        
        // Quit driver
        DriverManager.quitDriver();
    }
    
    /**
     * Test class teardown - runs after each test class
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        logger.info("Completed test class: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Suite teardown - runs once after all tests in the suite
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Force quit any remaining drivers
        DriverManager.forceQuitAll();
        
        // Flush reports
        ExtentReportManager.flushReports();
        
        logger.info("========== TEST SUITE COMPLETED ==========");
    }
    
    /**
     * Navigate to a specific URL
     */
    protected void navigateTo(String url) {
        getDriver().get(url);
        logger.info("Navigated to: {}", url);
    }
    
    /**
     * Navigate to base URL
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        getDriver().get(baseUrl);
        logger.info("Navigated to base URL: {}", baseUrl);
    }
}
