package com.automation.driver;

import com.automation.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver Manager - Manages WebDriver instances using ThreadLocal for parallel execution.
 * Supports Chrome, Firefox, Edge, Safari browsers.
 * Supports headless mode, remote execution, and browser attachment.
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // For browser attachment - shared driver instance
    private static WebDriver sharedDriver = null;
    private static boolean isSharedDriverInitialized = false;
    
    private DriverManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get the WebDriver instance for the current thread
     */
    public static WebDriver getDriver() {
        if (config.isAttachBrowserMode() && isSharedDriverInitialized) {
            return sharedDriver;
        }
        return driverThreadLocal.get();
    }
    
    /**
     * Initialize WebDriver based on configuration
     */
    public static void initDriver() {
        if (config.isAttachBrowserMode()) {
            initAttachedBrowser();
        } else if (config.isRemoteExecution()) {
            initRemoteDriver();
        } else {
            initLocalDriver();
        }
        
        configureDriver();
        logger.info("WebDriver initialized successfully");
    }
    
    /**
     * Initialize local WebDriver
     */
    private static void initLocalDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        
        WebDriver driver;
        
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = getChromeOptions(headless);
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = getFirefoxOptions(headless);
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = getEdgeOptions(headless);
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                driver = new SafariDriver();
                break;
                
            default:
                logger.error("Unsupported browser: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        
        driverThreadLocal.set(driver);
        logger.info("Local {} driver initialized (headless: {})", browser, headless);
    }
    
    /**
     * Initialize Remote WebDriver (Selenium Grid)
     */
    private static void initRemoteDriver() {
        String browser = config.getBrowser().toLowerCase();
        String remoteUrl = config.getRemoteUrl();
        boolean headless = config.isHeadless();
        
        try {
            WebDriver driver;
            
            switch (browser) {
                case "chrome":
                    driver = new RemoteWebDriver(new URL(remoteUrl), getChromeOptions(headless));
                    break;
                case "firefox":
                    driver = new RemoteWebDriver(new URL(remoteUrl), getFirefoxOptions(headless));
                    break;
                case "edge":
                    driver = new RemoteWebDriver(new URL(remoteUrl), getEdgeOptions(headless));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser for remote: " + browser);
            }
            
            driverThreadLocal.set(driver);
            logger.info("Remote {} driver initialized at: {}", browser, remoteUrl);
            
        } catch (MalformedURLException e) {
            logger.error("Invalid remote URL: {}", remoteUrl, e);
            throw new RuntimeException("Invalid remote URL: " + remoteUrl, e);
        }
    }
    
    /**
     * Initialize browser in attach mode (connect to existing browser session)
     * This is useful for debugging and saves time by reusing browser sessions
     */
    private static void initAttachedBrowser() {
        if (isSharedDriverInitialized && sharedDriver != null) {
            logger.info("Reusing existing browser session");
            return;
        }
        
        int debugPort = config.getDebugPort();
        
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + debugPort);
        
        try {
            sharedDriver = new ChromeDriver(options);
            isSharedDriverInitialized = true;
            logger.info("Attached to existing Chrome browser on port: {}", debugPort);
        } catch (Exception e) {
            logger.warn("Could not attach to existing browser. Starting new browser with debug port.");
            startBrowserWithDebugPort(debugPort);
        }
    }
    
    /**
     * Start a new browser with remote debugging enabled
     */
    public static void startBrowserWithDebugPort(int debugPort) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = getChromeOptions(config.isHeadless());
        options.addArguments("--remote-debugging-port=" + debugPort);
        options.addArguments("--user-data-dir=chrome_debug_profile");
        
        sharedDriver = new ChromeDriver(options);
        isSharedDriverInitialized = true;
        
        logger.info("Started Chrome with remote debugging on port: {}", debugPort);
        logger.info("You can now attach to this browser in subsequent test runs");
    }
    
    /**
     * Configure driver timeouts and window
     */
    private static void configureDriver() {
        WebDriver driver = getDriver();
        if (driver == null) return;
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        
        if (config.getBooleanProperty("browser.maximize")) {
            driver.manage().window().maximize();
        }
    }
    
    /**
     * Get Chrome options
     */
    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Set preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        return options;
    }
    
    /**
     * Get Firefox options
     */
    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        return options;
    }
    
    /**
     * Get Edge options
     */
    private static EdgeOptions getEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        return options;
    }
    
    /**
     * Quit the WebDriver instance
     */
    public static void quitDriver() {
        // Don't quit if in attach mode (keep browser open for reuse)
        if (config.isAttachBrowserMode()) {
            logger.info("Browser kept open for reuse (attach mode enabled)");
            return;
        }
        
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Force quit all drivers (including shared driver in attach mode)
     */
    public static void forceQuitAll() {
        // Quit thread-local driver
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error quitting thread-local driver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
        
        // Quit shared driver
        if (sharedDriver != null) {
            try {
                sharedDriver.quit();
            } catch (Exception e) {
                logger.error("Error quitting shared driver", e);
            } finally {
                sharedDriver = null;
                isSharedDriverInitialized = false;
            }
        }
        
        logger.info("All WebDriver instances quit");
    }
}
