package com.automation.api;

import com.automation.config.ConfigManager;
import com.automation.listeners.TestListener;
import com.automation.reports.ExtentReportManager;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base API Test - Abstract class for API tests.
 * Provides common setup, teardown, and assertion methods for API testing.
 */
@Listeners(TestListener.class)
public abstract class BaseApiTest {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final ConfigManager config = ConfigManager.getInstance();
    protected ApiClient apiClient;
    
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========== API TEST SUITE STARTED ==========");
        ExtentReportManager.initReports();
    }
    
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        logger.info("Starting API test class: {}", this.getClass().getSimpleName());
        apiClient = new ApiClient();
    }
    
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        // Reset API client for each test if needed
        logger.debug("Preparing for API test method");
    }
    
    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        logger.debug("API test method completed");
    }
    
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        logger.info("Completed API test class: {}", this.getClass().getSimpleName());
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentReportManager.flushReports();
        logger.info("========== API TEST SUITE COMPLETED ==========");
    }
    
    // ==================== Assertion Helper Methods ====================
    
    /**
     * Assert response status code
     */
    protected void assertStatusCode(Response response, int expectedStatusCode) {
        assertThat(response.getStatusCode())
            .as("Status code should be " + expectedStatusCode)
            .isEqualTo(expectedStatusCode);
        logger.info("Status code assertion passed: {}", expectedStatusCode);
    }
    
    /**
     * Assert response contains key
     */
    protected void assertResponseContainsKey(Response response, String key) {
        String value = response.jsonPath().getString(key);
        assertThat(value)
            .as("Response should contain key: " + key)
            .isNotNull();
        logger.info("Response contains key: {}", key);
    }
    
    /**
     * Assert response key equals value
     */
    protected void assertResponseKeyEquals(Response response, String key, Object expectedValue) {
        Object actualValue = response.jsonPath().get(key);
        assertThat(String.valueOf(actualValue))
            .as("Response key '" + key + "' should equal " + expectedValue)
            .isEqualTo(String.valueOf(expectedValue));
        logger.info("Response key '{}' equals expected value: {}", key, expectedValue);
    }
    
    /**
     * Assert response body contains text
     */
    protected void assertResponseBodyContains(Response response, String text) {
        assertThat(response.getBody().asString())
            .as("Response body should contain: " + text)
            .contains(text);
        logger.info("Response body contains: {}", text);
    }
    
    /**
     * Assert response time is less than threshold
     */
    protected void assertResponseTimeLessThan(Response response, long maxMilliseconds) {
        assertThat(response.getTime())
            .as("Response time should be less than " + maxMilliseconds + "ms")
            .isLessThan(maxMilliseconds);
        logger.info("Response time ({} ms) is less than {} ms", response.getTime(), maxMilliseconds);
    }
    
    /**
     * Assert response header exists
     */
    protected void assertHeaderExists(Response response, String headerName) {
        assertThat(response.getHeader(headerName))
            .as("Response should have header: " + headerName)
            .isNotNull();
        logger.info("Response has header: {}", headerName);
    }
    
    /**
     * Assert response header value
     */
    protected void assertHeaderValue(Response response, String headerName, String expectedValue) {
        assertThat(response.getHeader(headerName))
            .as("Header '" + headerName + "' should equal " + expectedValue)
            .isEqualTo(expectedValue);
        logger.info("Header '{}' equals: {}", headerName, expectedValue);
    }
    
    /**
     * Assert response content type
     */
    protected void assertContentType(Response response, String expectedContentType) {
        assertThat(response.getContentType())
            .as("Content-Type should contain: " + expectedContentType)
            .contains(expectedContentType);
        logger.info("Content-Type is: {}", response.getContentType());
    }
    
    /**
     * Assert response is JSON
     */
    protected void assertResponseIsJson(Response response) {
        assertContentType(response, "application/json");
    }
    
    /**
     * Assert array size in response
     */
    protected void assertArraySize(Response response, String jsonPath, int expectedSize) {
        java.util.List<?> list = response.jsonPath().getList(jsonPath);
        assertThat(list)
            .as("Array at '" + jsonPath + "' should have size " + expectedSize)
            .hasSize(expectedSize);
        logger.info("Array at '{}' has size: {}", jsonPath, expectedSize);
    }
    
    /**
     * Assert array is not empty
     */
    protected void assertArrayNotEmpty(Response response, String jsonPath) {
        java.util.List<?> list = response.jsonPath().getList(jsonPath);
        assertThat(list)
            .as("Array at '" + jsonPath + "' should not be empty")
            .isNotEmpty();
        logger.info("Array at '{}' is not empty", jsonPath);
    }
    
    /**
     * Get value from response
     */
    protected <T> T getResponseValue(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }
    
    /**
     * Log response details
     */
    protected void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Time: {} ms", response.getTime());
        logger.debug("Response Body: {}", response.getBody().asPrettyString());
    }
}
