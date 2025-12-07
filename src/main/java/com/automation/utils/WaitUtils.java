package com.automation.utils;

import com.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Wait Utilities - Provides various wait methods for Selenium WebDriver.
 */
public class WaitUtils {
    
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private WaitUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get default WebDriverWait instance
     */
    public static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
    }
    
    /**
     * Get WebDriverWait with custom timeout
     */
    public static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for element to be visible: {}", locator);
            return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", locator);
            throw new RuntimeException("Element not visible: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be visible with custom timeout
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            logger.debug("Waiting for element to be visible: {} (timeout: {}s)", locator, timeoutSeconds);
            return getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible within {} seconds: {}", timeoutSeconds, locator);
            throw new RuntimeException("Element not visible: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for element to be clickable: {}", locator);
            return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: {}", locator);
            throw new RuntimeException("Element not clickable: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            logger.debug("Waiting for element to be clickable: {} (timeout: {}s)", locator, timeoutSeconds);
            return getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within {} seconds: {}", timeoutSeconds, locator);
            throw new RuntimeException("Element not clickable: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for element to be present: {}", locator);
            return getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not present within timeout: {}", locator);
            throw new RuntimeException("Element not present: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be invisible
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for element to be invisible: {}", locator);
            return getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element still visible after timeout: {}", locator);
            throw new RuntimeException("Element still visible: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be invisible with custom timeout
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            logger.debug("Waiting for element to be invisible: {} (timeout: {}s)", locator, timeoutSeconds);
            return getWait(driver, timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element still visible after {} seconds: {}", timeoutSeconds, locator);
            throw new RuntimeException("Element still visible: " + locator, e);
        }
    }
    
    /**
     * Wait for all elements to be visible
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for all elements to be visible: {}", locator);
            return getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            logger.error("Elements not visible within timeout: {}", locator);
            throw new RuntimeException("Elements not visible: " + locator, e);
        }
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text) {
        try {
            logger.debug("Waiting for text '{}' in element: {}", text, locator);
            return getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            logger.error("Text '{}' not present in element within timeout: {}", text, locator);
            throw new RuntimeException("Text not present: " + text, e);
        }
    }
    
    /**
     * Wait for attribute to contain value
     */
    public static boolean waitForAttributeContains(WebDriver driver, By locator, String attribute, String value) {
        try {
            logger.debug("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
            return getWait(driver).until(ExpectedConditions.attributeContains(locator, attribute, value));
        } catch (TimeoutException e) {
            logger.error("Attribute '{}' does not contain '{}' within timeout: {}", attribute, value, locator);
            throw new RuntimeException("Attribute condition not met", e);
        }
    }
    
    /**
     * Wait for URL to contain text
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlPart) {
        try {
            logger.debug("Waiting for URL to contain: {}", urlPart);
            return getWait(driver).until(ExpectedConditions.urlContains(urlPart));
        } catch (TimeoutException e) {
            logger.error("URL does not contain '{}' within timeout", urlPart);
            throw new RuntimeException("URL condition not met: " + urlPart, e);
        }
    }
    
    /**
     * Wait for URL to match pattern
     */
    public static boolean waitForUrlMatches(WebDriver driver, String regex) {
        try {
            logger.debug("Waiting for URL to match: {}", regex);
            return getWait(driver).until(ExpectedConditions.urlMatches(regex));
        } catch (TimeoutException e) {
            logger.error("URL does not match '{}' within timeout", regex);
            throw new RuntimeException("URL pattern not matched: " + regex, e);
        }
    }
    
    /**
     * Wait for page title to contain text
     */
    public static boolean waitForTitleContains(WebDriver driver, String title) {
        try {
            logger.debug("Waiting for title to contain: {}", title);
            return getWait(driver).until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException e) {
            logger.error("Title does not contain '{}' within timeout", title);
            throw new RuntimeException("Title condition not met: " + title, e);
        }
    }
    
    /**
     * Wait for alert to be present
     */
    public static Alert waitForAlert(WebDriver driver) {
        try {
            logger.debug("Waiting for alert to be present");
            return getWait(driver).until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            logger.error("Alert not present within timeout");
            throw new RuntimeException("Alert not present", e);
        }
    }
    
    /**
     * Wait for frame to be available and switch to it
     */
    public static WebDriver waitForFrameAndSwitch(WebDriver driver, By locator) {
        try {
            logger.debug("Waiting for frame and switching: {}", locator);
            return getWait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
        } catch (TimeoutException e) {
            logger.error("Frame not available within timeout: {}", locator);
            throw new RuntimeException("Frame not available: " + locator, e);
        }
    }
    
    /**
     * Wait for number of windows to be
     */
    public static boolean waitForNumberOfWindows(WebDriver driver, int numberOfWindows) {
        try {
            logger.debug("Waiting for {} windows", numberOfWindows);
            return getWait(driver).until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
        } catch (TimeoutException e) {
            logger.error("Expected {} windows not present within timeout", numberOfWindows);
            throw new RuntimeException("Window count condition not met", e);
        }
    }
    
    /**
     * Wait for element to be stale
     */
    public static boolean waitForElementStale(WebDriver driver, WebElement element) {
        try {
            logger.debug("Waiting for element to be stale");
            return getWait(driver).until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            logger.error("Element not stale within timeout");
            throw new RuntimeException("Element not stale", e);
        }
    }
    
    /**
     * Fluent wait with custom polling interval
     */
    public static <T> T fluentWait(WebDriver driver, Function<WebDriver, T> condition, 
                                    int timeoutSeconds, int pollingMillis) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(timeoutSeconds))
            .pollingEvery(Duration.ofMillis(pollingMillis))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);
        
        return wait.until(condition);
    }
    
    /**
     * Wait for page to load completely (document.readyState)
     */
    public static void waitForPageLoad(WebDriver driver) {
        try {
            logger.debug("Waiting for page to load completely");
            getWait(driver).until(webDriver -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return js.executeScript("return document.readyState").equals("complete");
            });
        } catch (TimeoutException e) {
            logger.warn("Page load timeout - continuing anyway");
        }
    }
    
    /**
     * Wait for jQuery to complete (if jQuery is present)
     */
    public static void waitForJQuery(WebDriver driver) {
        try {
            logger.debug("Waiting for jQuery to complete");
            getWait(driver).until(webDriver -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Boolean) js.executeScript(
                    "return (typeof jQuery === 'undefined') || (jQuery.active === 0)");
            });
        } catch (TimeoutException e) {
            logger.warn("jQuery wait timeout - continuing anyway");
        }
    }
    
    /**
     * Wait for Angular to complete (if Angular is present)
     */
    public static void waitForAngular(WebDriver driver) {
        try {
            logger.debug("Waiting for Angular to complete");
            getWait(driver).until(webDriver -> {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                return (Boolean) js.executeScript(
                    "return (typeof angular === 'undefined') || " +
                    "(angular.element(document.body).injector() === undefined) || " +
                    "(angular.element(document.body).injector().get('$http').pendingRequests.length === 0)");
            });
        } catch (TimeoutException e) {
            logger.warn("Angular wait timeout - continuing anyway");
        }
    }
    
    /**
     * Simple thread sleep (use sparingly)
     */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted", e);
        }
    }
}
