package com.automation.listeners;

import com.automation.config.ConfigManager;
import com.automation.driver.DriverManager;
import com.automation.reports.ExtentReportManager;
import com.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * Test Listener - Implements TestNG ITestListener for test lifecycle events.
 * Handles reporting, screenshots, and logging for test execution.
 */
public class TestListener implements ITestListener {
    
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Started: {}", context.getName());
        logger.info("========================================");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Passed: {}", context.getPassedTests().size());
        logger.info("Failed: {}", context.getFailedTests().size());
        logger.info("Skipped: {}", context.getSkippedTests().size());
        logger.info("========================================");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        
        logger.info("----------------------------------------");
        logger.info("Test Started: {}.{}", className, testName);
        logger.info("----------------------------------------");
        
        // Create test in Extent Report
        ExtentReportManager.createTest(testName, result.getMethod().getDescription());
        
        // Assign categories if present
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            ExtentReportManager.assignCategory(groups);
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        logger.info("Test PASSED: {}", testName);
        logger.info("Duration: {} ms", result.getEndMillis() - result.getStartMillis());
        
        // Log to Extent Report
        ExtentReportManager.logPass("Test passed successfully");
        
        // Take screenshot on pass if configured
        if (config.getBooleanProperty("screenshot.on.pass")) {
            captureScreenshot(result, "Pass");
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        logger.error("Test FAILED: {}", testName);
        if (throwable != null) {
            logger.error("Failure Reason: {}", throwable.getMessage());
        }
        
        // Log to Extent Report
        ExtentReportManager.logFail("Test failed", throwable);
        
        // Take screenshot on failure
        if (config.shouldTakeScreenshotOnFailure()) {
            captureScreenshot(result, "Failure");
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        logger.warn("Test SKIPPED: {}", testName);
        if (throwable != null) {
            logger.warn("Skip Reason: {}", throwable.getMessage());
        }
        
        // Log to Extent Report
        ExtentReportManager.logSkip("Test skipped" + 
            (throwable != null ? ": " + throwable.getMessage() : ""));
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test failed but within success percentage: {}", testName);
    }
    
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test FAILED with timeout: {}", testName);
        
        ExtentReportManager.logFail("Test failed due to timeout");
        
        if (config.shouldTakeScreenshotOnFailure()) {
            captureScreenshot(result, "Timeout");
        }
    }
    
    /**
     * Capture screenshot and attach to reports
     */
    private void captureScreenshot(ITestResult result, String status) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                // Take screenshot
                byte[] screenshot = ScreenshotUtils.takeScreenshot(driver);
                String screenshotPath = ScreenshotUtils.saveScreenshot(screenshot, 
                    result.getMethod().getMethodName() + "_" + status);
                
                // Attach to Allure report
                Allure.addAttachment(status + " Screenshot", 
                    new ByteArrayInputStream(screenshot));
                
                // Attach to Extent report
                ExtentReportManager.addScreenshot(screenshotPath, status + " Screenshot");
                
                logger.info("Screenshot captured: {}", screenshotPath);
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }
}
