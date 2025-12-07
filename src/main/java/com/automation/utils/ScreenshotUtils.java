package com.automation.utils;

import com.automation.config.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot Utilities - Provides methods for capturing and saving screenshots.
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private ScreenshotUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Take screenshot and return as byte array
     */
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }
    
    /**
     * Take screenshot and return as File
     */
    public static File takeScreenshotAsFile(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.FILE);
        } catch (Exception e) {
            logger.error("Failed to take screenshot as file", e);
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }
    
    /**
     * Take screenshot and return as Base64 string
     */
    public static String takeScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to take screenshot as Base64", e);
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }
    
    /**
     * Save screenshot to file and return path
     */
    public static String saveScreenshot(byte[] screenshot, String testName) {
        String screenshotDir = config.getScreenshotPath();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = sanitizeFileName(testName) + "_" + timestamp + ".png";
        String filePath = screenshotDir + File.separator + fileName;
        
        try {
            // Create directory if it doesn't exist
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Save screenshot
            File file = new File(filePath);
            FileUtils.writeByteArrayToFile(file, screenshot);
            
            logger.info("Screenshot saved: {}", filePath);
            return filePath;
            
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", filePath, e);
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }
    
    /**
     * Save screenshot from File to destination
     */
    public static String saveScreenshot(File screenshot, String testName) {
        String screenshotDir = config.getScreenshotPath();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = sanitizeFileName(testName) + "_" + timestamp + ".png";
        String filePath = screenshotDir + File.separator + fileName;
        
        try {
            // Create directory if it doesn't exist
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Copy screenshot to destination
            File destination = new File(filePath);
            FileUtils.copyFile(screenshot, destination);
            
            logger.info("Screenshot saved: {}", filePath);
            return filePath;
            
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", filePath, e);
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }
    
    /**
     * Take screenshot of specific element
     */
    public static byte[] takeElementScreenshot(WebElement element) {
        try {
            return element.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take element screenshot", e);
            throw new RuntimeException("Failed to take element screenshot", e);
        }
    }
    
    /**
     * Save element screenshot to file
     */
    public static String saveElementScreenshot(WebElement element, String elementName) {
        byte[] screenshot = takeElementScreenshot(element);
        return saveScreenshot(screenshot, "element_" + elementName);
    }
    
    /**
     * Take full page screenshot (for browsers that support it)
     */
    public static byte[] takeFullPageScreenshot(WebDriver driver) {
        // Note: Full page screenshot support varies by browser
        // Chrome DevTools Protocol can be used for full page screenshots
        return takeScreenshot(driver);
    }
    
    /**
     * Convert byte array to BufferedImage
     */
    public static BufferedImage byteArrayToImage(byte[] imageBytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bis);
        } catch (IOException e) {
            logger.error("Failed to convert byte array to image", e);
            throw new RuntimeException("Failed to convert byte array to image", e);
        }
    }
    
    /**
     * Sanitize file name by removing invalid characters
     */
    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "screenshot";
        }
        // Remove invalid characters for file names
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    
    /**
     * Get screenshot directory path
     */
    public static String getScreenshotDirectory() {
        return config.getScreenshotPath();
    }
    
    /**
     * Clean up old screenshots (older than specified days)
     */
    public static void cleanupOldScreenshots(int daysOld) {
        String screenshotDir = config.getScreenshotPath();
        File directory = new File(screenshotDir);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        
        long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60L * 60L * 1000L);
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        logger.debug("Deleted old screenshot: {}", file.getName());
                    }
                }
            }
        }
    }
}
