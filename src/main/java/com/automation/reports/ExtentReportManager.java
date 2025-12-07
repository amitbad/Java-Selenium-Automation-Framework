package com.automation.reports;

import com.automation.config.ConfigManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extent Report Manager - Manages Extent Reports for test execution.
 * Provides methods to create, update, and flush reports.
 */
public class ExtentReportManager {
    
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    private static final String REPORT_DIR = "target/reports/";
    private static String reportPath;
    
    private ExtentReportManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize Extent Reports
     */
    public static synchronized void initReports() {
        if (extentReports == null) {
            // Create report directory
            File reportDir = new File(REPORT_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            // Generate report file name with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportPath = REPORT_DIR + "TestReport_" + timestamp + ".html";
            
            // Configure Spark Reporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            sparkReporter.config().setEncoding("UTF-8");
            
            // Initialize ExtentReports
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Add system info
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", config.getBrowser());
            extentReports.setSystemInfo("Environment", config.getProperty("environment", "QA"));
            extentReports.setSystemInfo("Base URL", config.getBaseUrl());
            
            logger.info("Extent Reports initialized: {}", reportPath);
        }
    }
    
    /**
     * Create a new test in the report
     */
    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest test = extentReports.createTest(testName);
        extentTest.set(test);
        logger.debug("Created test in report: {}", testName);
        return test;
    }
    
    /**
     * Create a new test with description
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = extentReports.createTest(testName, description);
        extentTest.set(test);
        logger.debug("Created test in report: {} - {}", testName, description);
        return test;
    }
    
    /**
     * Get current test
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Log pass status
     */
    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }
    
    /**
     * Log fail status
     */
    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
    
    /**
     * Log fail status with exception
     */
    public static void logFail(String message, Throwable throwable) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.FAIL, message);
            test.log(Status.FAIL, throwable);
        }
    }
    
    /**
     * Log skip status
     */
    public static void logSkip(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.SKIP, message);
        }
    }
    
    /**
     * Log info status
     */
    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log warning status
     */
    public static void logWarning(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.WARNING, message);
        }
    }
    
    /**
     * Add screenshot to report
     */
    public static void addScreenshot(String screenshotPath, String title) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                test.addScreenCaptureFromPath(screenshotPath, title);
            } catch (Exception e) {
                logger.error("Failed to add screenshot to report", e);
            }
        }
    }
    
    /**
     * Add screenshot as Base64
     */
    public static void addScreenshotBase64(String base64Screenshot, String title) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                test.log(Status.INFO, title, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } catch (Exception e) {
                logger.error("Failed to add Base64 screenshot to report", e);
            }
        }
    }
    
    /**
     * Create a node (sub-test) under current test
     */
    public static ExtentTest createNode(String nodeName) {
        ExtentTest test = getTest();
        if (test != null) {
            return test.createNode(nodeName);
        }
        return null;
    }
    
    /**
     * Assign category to current test
     */
    public static void assignCategory(String... categories) {
        ExtentTest test = getTest();
        if (test != null) {
            test.assignCategory(categories);
        }
    }
    
    /**
     * Assign author to current test
     */
    public static void assignAuthor(String... authors) {
        ExtentTest test = getTest();
        if (test != null) {
            test.assignAuthor(authors);
        }
    }
    
    /**
     * Assign device to current test
     */
    public static void assignDevice(String... devices) {
        ExtentTest test = getTest();
        if (test != null) {
            test.assignDevice(devices);
        }
    }
    
    /**
     * Flush reports - must be called at the end of test execution
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent Reports flushed: {}", reportPath);
        }
        extentTest.remove();
    }
    
    /**
     * Get report path
     */
    public static String getReportPath() {
        return reportPath;
    }
}
