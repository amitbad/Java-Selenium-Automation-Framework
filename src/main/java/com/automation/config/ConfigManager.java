package com.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration Manager - Singleton class for managing application configuration.
 * Supports loading from properties files and environment variables.
 * Environment variables take precedence over properties file values.
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;
    
    private static final String CONFIG_FILE = "src/main/resources/config/config.properties";
    private static final String ENV_FILE = "src/main/resources/config/env.properties";
    
    private ConfigManager() {
        properties = new Properties();
        loadConfiguration();
    }
    
    /**
     * Get singleton instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load configuration from properties files
     */
    private void loadConfiguration() {
        // Load default config
        loadPropertiesFile(CONFIG_FILE);
        
        // Load environment-specific config if exists (overrides defaults)
        if (Files.exists(Paths.get(ENV_FILE))) {
            loadPropertiesFile(ENV_FILE);
            logger.info("Loaded environment-specific configuration from env.properties");
        }
        
        // Environment variables override everything
        loadEnvironmentVariables();
        
        logger.info("Configuration loaded successfully");
    }
    
    /**
     * Load properties from a file
     */
    private void loadPropertiesFile(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            logger.debug("Loaded properties from: {}", filePath);
        } catch (IOException e) {
            logger.warn("Could not load properties file: {}. Using defaults.", filePath);
        }
    }
    
    /**
     * Override properties with environment variables
     */
    private void loadEnvironmentVariables() {
        // Map of property keys to environment variable names
        String[][] envMappings = {
            {"base.url", "BASE_URL"},
            {"api.base.url", "API_BASE_URL"},
            {"browser", "BROWSER"},
            {"headless", "HEADLESS"},
            {"remote.execution", "REMOTE_EXECUTION"},
            {"remote.url", "SELENIUM_GRID_URL"},
            {"test.username", "TEST_USERNAME"},
            {"test.password", "TEST_PASSWORD"},
            {"api.key", "API_KEY"},
            {"api.secret", "API_SECRET"},
            {"email.host", "EMAIL_HOST"},
            {"email.port", "EMAIL_PORT"},
            {"email.username", "EMAIL_USERNAME"},
            {"email.password", "EMAIL_PASSWORD"},
            {"email.from", "EMAIL_FROM"},
            {"email.to", "EMAIL_TO"}
        };
        
        for (String[] mapping : envMappings) {
            String envValue = System.getenv(mapping[1]);
            if (envValue != null && !envValue.isEmpty()) {
                properties.setProperty(mapping[0], envValue);
                logger.debug("Loaded {} from environment variable {}", mapping[0], mapping[1]);
            }
        }
    }
    
    /**
     * Get property value by key
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }
    
    /**
     * Get property value with default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property as boolean
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key, "false"));
    }
    
    /**
     * Get property as integer
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key, "0"));
    }
    
    /**
     * Get property as integer with default
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    // Convenience methods for common properties
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }
    
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }
    
    public boolean isRemoteExecution() {
        return getBooleanProperty("remote.execution");
    }
    
    public String getRemoteUrl() {
        return getProperty("remote.url");
    }
    
    public int getImplicitWait() {
        return getIntProperty("browser.implicit.wait", 10);
    }
    
    public int getExplicitWait() {
        return getIntProperty("browser.explicit.wait", 30);
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("browser.page.load.timeout", 60);
    }
    
    public boolean shouldTakeScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure");
    }
    
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "target/screenshots");
    }
    
    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }
    
    public boolean isAttachBrowserMode() {
        return getBooleanProperty("attach.browser");
    }
    
    public int getDebugPort() {
        return getIntProperty("debug.port", 9222);
    }
    
    public String getTestUsername() {
        return getProperty("test.username");
    }
    
    public String getTestPassword() {
        return getProperty("test.password");
    }
}
