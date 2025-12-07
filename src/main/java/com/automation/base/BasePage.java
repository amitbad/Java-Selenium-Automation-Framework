package com.automation.base;

import com.automation.config.ConfigManager;
import com.automation.driver.DriverManager;
import com.automation.locators.LocatorManager;
import com.automation.utils.WaitUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.List;

/**
 * Base Page - Abstract class providing common page object functionality.
 * All page objects should extend this class.
 */
public abstract class BasePage {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final ConfigManager config = ConfigManager.getInstance();
    protected final String locatorFile;
    
    /**
     * Constructor
     * @param locatorFile Name of the JSON locator file for this page
     */
    protected BasePage(String locatorFile) {
        this.locatorFile = locatorFile;
        logger.debug("Initialized page with locator file: {}", locatorFile);
    }
    
    /**
     * Get WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
    
    /**
     * Get locator by element name from JSON file
     */
    protected By getLocator(String elementName) {
        return LocatorManager.getLocator(locatorFile, elementName);
    }
    
    /**
     * Find element using locator name
     */
    protected WebElement findElement(String elementName) {
        By locator = getLocator(elementName);
        return WaitUtils.waitForElementVisible(getDriver(), locator);
    }
    
    /**
     * Find elements using locator name
     */
    protected List<WebElement> findElements(String elementName) {
        By locator = getLocator(elementName);
        return getDriver().findElements(locator);
    }
    
    /**
     * Click on element
     */
    @Step("Click on {elementName}")
    protected void click(String elementName) {
        try {
            WebElement element = findElement(elementName);
            WaitUtils.waitForElementClickable(getDriver(), getLocator(elementName));
            element.click();
            logger.info("Clicked on element: {}", elementName);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Element click intercepted, trying JavaScript click: {}", elementName);
            clickWithJS(elementName);
        } catch (Exception e) {
            logger.error("Failed to click on element: {}", elementName, e);
            throw new RuntimeException("Failed to click on element: " + elementName, e);
        }
    }
    
    /**
     * Click using JavaScript
     */
    @Step("JavaScript click on {elementName}")
    protected void clickWithJS(String elementName) {
        try {
            WebElement element = findElement(elementName);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", element);
            logger.info("JavaScript clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to JavaScript click on element: {}", elementName, e);
            throw new RuntimeException("Failed to JavaScript click on element: " + elementName, e);
        }
    }
    
    /**
     * Enter text in input field
     */
    @Step("Enter text '{text}' in {elementName}")
    protected void enterText(String elementName, String text) {
        try {
            WebElement element = findElement(elementName);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text in element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to enter text in element: {}", elementName, e);
            throw new RuntimeException("Failed to enter text in element: " + elementName, e);
        }
    }
    
    /**
     * Enter text without clearing
     */
    @Step("Append text '{text}' to {elementName}")
    protected void appendText(String elementName, String text) {
        try {
            WebElement element = findElement(elementName);
            element.sendKeys(text);
            logger.info("Appended text to element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to append text to element: {}", elementName, e);
            throw new RuntimeException("Failed to append text to element: " + elementName, e);
        }
    }
    
    /**
     * Get text from element
     */
    @Step("Get text from {elementName}")
    protected String getText(String elementName) {
        try {
            WebElement element = findElement(elementName);
            String text = element.getText();
            logger.info("Got text from element {}: {}", elementName, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", elementName, e);
            throw new RuntimeException("Failed to get text from element: " + elementName, e);
        }
    }
    
    /**
     * Get attribute value from element
     */
    @Step("Get attribute '{attribute}' from {elementName}")
    protected String getAttribute(String elementName, String attribute) {
        try {
            WebElement element = findElement(elementName);
            return element.getAttribute(attribute);
        } catch (Exception e) {
            logger.error("Failed to get attribute from element: {}", elementName, e);
            throw new RuntimeException("Failed to get attribute from element: " + elementName, e);
        }
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(String elementName) {
        try {
            WebElement element = getDriver().findElement(getLocator(elementName));
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(String elementName) {
        try {
            WebElement element = findElement(elementName);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is selected (for checkboxes/radio buttons)
     */
    protected boolean isElementSelected(String elementName) {
        try {
            WebElement element = findElement(elementName);
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Select dropdown by visible text
     */
    @Step("Select '{text}' from dropdown {elementName}")
    protected void selectByVisibleText(String elementName, String text) {
        try {
            WebElement element = findElement(elementName);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.info("Selected '{}' from dropdown: {}", text, elementName);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: {}", elementName, e);
            throw new RuntimeException("Failed to select from dropdown: " + elementName, e);
        }
    }
    
    /**
     * Select dropdown by value
     */
    @Step("Select value '{value}' from dropdown {elementName}")
    protected void selectByValue(String elementName, String value) {
        try {
            WebElement element = findElement(elementName);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Selected value '{}' from dropdown: {}", value, elementName);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: {}", elementName, e);
            throw new RuntimeException("Failed to select from dropdown: " + elementName, e);
        }
    }
    
    /**
     * Select dropdown by index
     */
    @Step("Select index {index} from dropdown {elementName}")
    protected void selectByIndex(String elementName, int index) {
        try {
            WebElement element = findElement(elementName);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.info("Selected index {} from dropdown: {}", index, elementName);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: {}", elementName, e);
            throw new RuntimeException("Failed to select from dropdown: " + elementName, e);
        }
    }
    
    /**
     * Hover over element
     */
    @Step("Hover over {elementName}")
    protected void hoverOver(String elementName) {
        try {
            WebElement element = findElement(elementName);
            Actions actions = new Actions(getDriver());
            actions.moveToElement(element).perform();
            logger.info("Hovered over element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", elementName, e);
            throw new RuntimeException("Failed to hover over element: " + elementName, e);
        }
    }
    
    /**
     * Double click on element
     */
    @Step("Double click on {elementName}")
    protected void doubleClick(String elementName) {
        try {
            WebElement element = findElement(elementName);
            Actions actions = new Actions(getDriver());
            actions.doubleClick(element).perform();
            logger.info("Double clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to double click on element: {}", elementName, e);
            throw new RuntimeException("Failed to double click on element: " + elementName, e);
        }
    }
    
    /**
     * Right click on element
     */
    @Step("Right click on {elementName}")
    protected void rightClick(String elementName) {
        try {
            WebElement element = findElement(elementName);
            Actions actions = new Actions(getDriver());
            actions.contextClick(element).perform();
            logger.info("Right clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to right click on element: {}", elementName, e);
            throw new RuntimeException("Failed to right click on element: " + elementName, e);
        }
    }
    
    /**
     * Scroll to element
     */
    @Step("Scroll to {elementName}")
    protected void scrollToElement(String elementName) {
        try {
            WebElement element = findElement(elementName);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            logger.info("Scrolled to element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", elementName, e);
            throw new RuntimeException("Failed to scroll to element: " + elementName, e);
        }
    }
    
    /**
     * Wait for element to be visible
     */
    protected void waitForElement(String elementName) {
        WaitUtils.waitForElementVisible(getDriver(), getLocator(elementName));
    }
    
    /**
     * Wait for element to be invisible
     */
    protected void waitForElementInvisible(String elementName) {
        WaitUtils.waitForElementInvisible(getDriver(), getLocator(elementName));
    }
    
    /**
     * Navigate to URL
     */
    @Step("Navigate to URL: {url}")
    protected void navigateTo(String url) {
        getDriver().get(url);
        logger.info("Navigated to: {}", url);
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    /**
     * Get page title
     */
    protected String getPageTitle() {
        return getDriver().getTitle();
    }
    
    /**
     * Refresh page
     */
    @Step("Refresh page")
    protected void refreshPage() {
        getDriver().navigate().refresh();
        logger.info("Page refreshed");
    }
    
    /**
     * Navigate back
     */
    @Step("Navigate back")
    protected void navigateBack() {
        getDriver().navigate().back();
        logger.info("Navigated back");
    }
    
    /**
     * Navigate forward
     */
    @Step("Navigate forward")
    protected void navigateForward() {
        getDriver().navigate().forward();
        logger.info("Navigated forward");
    }
    
    /**
     * Accept alert
     */
    @Step("Accept alert")
    protected void acceptAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.error("Failed to accept alert", e);
            throw new RuntimeException("Failed to accept alert", e);
        }
    }
    
    /**
     * Dismiss alert
     */
    @Step("Dismiss alert")
    protected void dismissAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().dismiss();
            logger.info("Alert dismissed");
        } catch (Exception e) {
            logger.error("Failed to dismiss alert", e);
            throw new RuntimeException("Failed to dismiss alert", e);
        }
    }
    
    /**
     * Get alert text
     */
    protected String getAlertText() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            return getDriver().switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("Failed to get alert text", e);
            throw new RuntimeException("Failed to get alert text", e);
        }
    }
    
    /**
     * Switch to frame by element name
     */
    @Step("Switch to frame: {elementName}")
    protected void switchToFrame(String elementName) {
        try {
            WebElement frame = findElement(elementName);
            getDriver().switchTo().frame(frame);
            logger.info("Switched to frame: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to switch to frame: {}", elementName, e);
            throw new RuntimeException("Failed to switch to frame: " + elementName, e);
        }
    }
    
    /**
     * Switch to default content
     */
    @Step("Switch to default content")
    protected void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
        logger.info("Switched to default content");
    }
    
    /**
     * Take screenshot and attach to Allure report
     */
    protected void takeScreenshot(String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
            logger.info("Screenshot taken: {}", name);
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
    }
}
