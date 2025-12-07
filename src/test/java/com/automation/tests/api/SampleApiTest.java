package com.automation.tests.api;

import com.automation.api.ApiClient;
import com.automation.api.BaseApiTest;
import com.automation.listeners.RetryAnalyzer;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Sample API Test - Demonstrates API testing capabilities.
 * Uses JSONPlaceholder as a sample API for demonstration.
 */
@Epic("API Testing")
@Feature("REST API")
public class SampleApiTest extends BaseApiTest {
    
    private static final String SAMPLE_API_BASE = "https://jsonplaceholder.typicode.com";
    
    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        super.beforeClass();
        // Override base URL for sample API
        apiClient = new ApiClient(SAMPLE_API_BASE);
    }
    
    @Test(description = "Verify GET request returns list of posts",
          groups = {"smoke", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET Requests")
    @Description("Test to verify GET request returns posts successfully")
    public void testGetPosts() {
        logger.info("Starting GET posts test");
        
        Response response = apiClient.get("/posts");
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseIsJson(response);
        assertArrayNotEmpty(response, "$");
        
        // Log response
        logResponse(response);
        
        logger.info("GET posts test completed");
    }
    
    @Test(description = "Verify GET request returns single post",
          groups = {"smoke", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET Requests")
    @Description("Test to verify GET request returns a single post by ID")
    public void testGetSinglePost() {
        logger.info("Starting GET single post test");
        
        Response response = apiClient.get("/posts/1");
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseIsJson(response);
        assertResponseContainsKey(response, "id");
        assertResponseContainsKey(response, "title");
        assertResponseContainsKey(response, "body");
        assertResponseKeyEquals(response, "id", 1);
        
        // Log response
        logResponse(response);
        
        logger.info("GET single post test completed");
    }
    
    @Test(description = "Verify POST request creates new resource",
          groups = {"smoke", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("POST Requests")
    @Description("Test to verify POST request creates a new post")
    public void testCreatePost() {
        logger.info("Starting POST create post test");
        
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Test Post Title");
        requestBody.put("body", "This is the test post body content");
        requestBody.put("userId", 1);
        
        Response response = apiClient.post("/posts", requestBody);
        
        // Assertions
        assertStatusCode(response, 201);
        assertResponseIsJson(response);
        assertResponseContainsKey(response, "id");
        assertResponseKeyEquals(response, "title", "Test Post Title");
        
        // Log response
        logResponse(response);
        
        logger.info("POST create post test completed");
    }
    
    @Test(description = "Verify PUT request updates resource",
          groups = {"regression", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("PUT Requests")
    @Description("Test to verify PUT request updates an existing post")
    public void testUpdatePost() {
        logger.info("Starting PUT update post test");
        
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1);
        requestBody.put("title", "Updated Post Title");
        requestBody.put("body", "This is the updated post body content");
        requestBody.put("userId", 1);
        
        Response response = apiClient.put("/posts/1", requestBody);
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseIsJson(response);
        assertResponseKeyEquals(response, "title", "Updated Post Title");
        
        // Log response
        logResponse(response);
        
        logger.info("PUT update post test completed");
    }
    
    @Test(description = "Verify PATCH request partially updates resource",
          groups = {"regression", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("PATCH Requests")
    @Description("Test to verify PATCH request partially updates a post")
    public void testPatchPost() {
        logger.info("Starting PATCH update post test");
        
        // Create request body with only fields to update
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Patched Post Title");
        
        Response response = apiClient.patch("/posts/1", requestBody);
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseIsJson(response);
        assertResponseKeyEquals(response, "title", "Patched Post Title");
        
        // Log response
        logResponse(response);
        
        logger.info("PATCH update post test completed");
    }
    
    @Test(description = "Verify DELETE request removes resource",
          groups = {"regression", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("DELETE Requests")
    @Description("Test to verify DELETE request removes a post")
    public void testDeletePost() {
        logger.info("Starting DELETE post test");
        
        Response response = apiClient.delete("/posts/1");
        
        // Assertions
        assertStatusCode(response, 200);
        
        // Log response
        logResponse(response);
        
        logger.info("DELETE post test completed");
    }
    
    @Test(description = "Verify GET request with query parameters",
          groups = {"regression", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("GET Requests")
    @Description("Test to verify GET request with query parameters")
    public void testGetPostsWithQueryParams() {
        logger.info("Starting GET posts with query params test");
        
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", 1);
        
        Response response = apiClient.get("/posts", queryParams);
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseIsJson(response);
        assertArrayNotEmpty(response, "$");
        
        // Log response
        logResponse(response);
        
        logger.info("GET posts with query params test completed");
    }
    
    @Test(description = "Verify response time is acceptable",
          groups = {"performance", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.MINOR)
    @Story("Performance")
    @Description("Test to verify API response time is within acceptable limits")
    public void testResponseTime() {
        logger.info("Starting response time test");
        
        Response response = apiClient.get("/posts/1");
        
        // Assertions
        assertStatusCode(response, 200);
        assertResponseTimeLessThan(response, 5000); // 5 seconds max
        
        // Log response
        logResponse(response);
        
        logger.info("Response time test completed");
    }
    
    @Test(description = "Verify 404 for non-existent resource",
          groups = {"negative", "api"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Error Handling")
    @Description("Test to verify 404 response for non-existent resource")
    public void testNotFoundResponse() {
        logger.info("Starting 404 not found test");
        
        Response response = apiClient.get("/posts/99999");
        
        // Assertions
        assertStatusCode(response, 404);
        
        // Log response
        logResponse(response);
        
        logger.info("404 not found test completed");
    }
}
