package com.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * JSON Utilities - Provides methods for JSON parsing and manipulation.
 */
public class JsonUtils {
    
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    
    private JsonUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Convert object to JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert object to JSON", e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert object to pretty JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert object to pretty JSON", e);
            throw new RuntimeException("Failed to convert object to pretty JSON", e);
        }
    }
    
    /**
     * Parse JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON to object", e);
            throw new RuntimeException("Failed to parse JSON to object", e);
        }
    }
    
    /**
     * Parse JSON string to object with TypeReference (for generics)
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON to object", e);
            throw new RuntimeException("Failed to parse JSON to object", e);
        }
    }
    
    /**
     * Read JSON file to object
     */
    public static <T> T readJsonFile(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON file to JsonNode
     */
    public static JsonNode readJsonFile(String filePath) {
        try {
            return objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Write object to JSON file
     */
    public static void writeJsonFile(String filePath, Object object) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
            logger.info("JSON written to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to write JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write JSON file: " + filePath, e);
        }
    }
    
    /**
     * Parse JSON string to Map
     */
    public static Map<String, Object> toMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON to Map", e);
            throw new RuntimeException("Failed to parse JSON to Map", e);
        }
    }
    
    /**
     * Get value from JSON by path
     */
    public static String getValueByPath(String json, String path) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String[] pathParts = path.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String part : pathParts) {
                if (currentNode == null) {
                    return null;
                }
                currentNode = currentNode.get(part);
            }
            
            return currentNode != null ? currentNode.asText() : null;
        } catch (JsonProcessingException e) {
            logger.error("Failed to get value by path: {}", path, e);
            throw new RuntimeException("Failed to get value by path: " + path, e);
        }
    }
    
    /**
     * Check if string is valid JSON
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    /**
     * Merge two JSON objects
     */
    public static String mergeJson(String json1, String json2) {
        try {
            Map<String, Object> map1 = toMap(json1);
            Map<String, Object> map2 = toMap(json2);
            map1.putAll(map2);
            return toJson(map1);
        } catch (Exception e) {
            logger.error("Failed to merge JSON objects", e);
            throw new RuntimeException("Failed to merge JSON objects", e);
        }
    }
}
