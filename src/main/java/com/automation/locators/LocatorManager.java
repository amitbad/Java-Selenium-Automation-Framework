package com.automation.locators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Locator Manager - Manages JSON-based locators for page objects.
 * Provides a centralized way to load and retrieve locators from JSON files.
 * 
 * JSON Locator Format:
 * {
 *   "pageName": "LoginPage",
 *   "locators": {
 *     "elementName": {
 *       "type": "id|css|xpath|name|className|linkText|partialLinkText|tagName",
 *       "value": "locator_value",
 *       "description": "optional description"
 *     }
 *   }
 * }
 */
public class LocatorManager {
    
    private static final Logger logger = LogManager.getLogger(LocatorManager.class);
    private static final String LOCATORS_PATH = "src/main/resources/locators/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // Cache for loaded locators
    private static final Map<String, Map<String, LocatorInfo>> locatorCache = new ConcurrentHashMap<>();
    
    /**
     * Load locators from a JSON file
     * @param fileName Name of the JSON file (without path)
     * @return Map of element names to LocatorInfo objects
     */
    public static Map<String, LocatorInfo> loadLocators(String fileName) {
        if (locatorCache.containsKey(fileName)) {
            logger.debug("Returning cached locators for: {}", fileName);
            return locatorCache.get(fileName);
        }
        
        Map<String, LocatorInfo> locators = new HashMap<>();
        String filePath = LOCATORS_PATH + fileName;
        
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.error("Locator file not found: {}", filePath);
                throw new RuntimeException("Locator file not found: " + filePath);
            }
            
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode locatorsNode = rootNode.get("locators");
            
            if (locatorsNode != null) {
                locatorsNode.fields().forEachRemaining(entry -> {
                    String elementName = entry.getKey();
                    JsonNode elementNode = entry.getValue();
                    
                    String type = elementNode.get("type").asText();
                    String value = elementNode.get("value").asText();
                    String description = elementNode.has("description") 
                        ? elementNode.get("description").asText() 
                        : "";
                    
                    locators.put(elementName, new LocatorInfo(type, value, description));
                });
            }
            
            locatorCache.put(fileName, locators);
            logger.info("Loaded {} locators from: {}", locators.size(), fileName);
            
        } catch (IOException e) {
            logger.error("Error loading locators from: {}", filePath, e);
            throw new RuntimeException("Failed to load locators from: " + filePath, e);
        }
        
        return locators;
    }
    
    /**
     * Get a specific locator as Selenium By object
     * @param fileName Name of the JSON file
     * @param elementName Name of the element
     * @return Selenium By locator
     */
    public static By getLocator(String fileName, String elementName) {
        Map<String, LocatorInfo> locators = loadLocators(fileName);
        LocatorInfo locatorInfo = locators.get(elementName);
        
        if (locatorInfo == null) {
            logger.error("Locator '{}' not found in file: {}", elementName, fileName);
            throw new RuntimeException("Locator not found: " + elementName + " in " + fileName);
        }
        
        return locatorInfo.toBy();
    }
    
    /**
     * Get locator info (for debugging/logging)
     */
    public static LocatorInfo getLocatorInfo(String fileName, String elementName) {
        Map<String, LocatorInfo> locators = loadLocators(fileName);
        return locators.get(elementName);
    }
    
    /**
     * Clear the locator cache (useful for reloading during development)
     */
    public static void clearCache() {
        locatorCache.clear();
        logger.info("Locator cache cleared");
    }
    
    /**
     * Inner class to hold locator information
     */
    public static class LocatorInfo {
        private final String type;
        private final String value;
        private final String description;
        
        public LocatorInfo(String type, String value, String description) {
            this.type = type.toLowerCase();
            this.value = value;
            this.description = description;
        }
        
        public String getType() {
            return type;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getDescription() {
            return description;
        }
        
        /**
         * Convert to Selenium By object
         */
        public By toBy() {
            switch (type) {
                case "id":
                    return By.id(value);
                case "css":
                case "cssselector":
                    return By.cssSelector(value);
                case "xpath":
                    return By.xpath(value);
                case "name":
                    return By.name(value);
                case "classname":
                case "class":
                    return By.className(value);
                case "linktext":
                    return By.linkText(value);
                case "partiallinktext":
                    return By.partialLinkText(value);
                case "tagname":
                case "tag":
                    return By.tagName(value);
                default:
                    throw new IllegalArgumentException("Unknown locator type: " + type);
            }
        }
        
        @Override
        public String toString() {
            return String.format("LocatorInfo{type='%s', value='%s', description='%s'}", 
                type, value, description);
        }
    }
}
