package com.automation.api;

import com.automation.config.ConfigManager;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * API Client - Provides REST API testing capabilities using REST Assured.
 * Supports GET, POST, PUT, PATCH, DELETE methods with various configurations.
 */
public class ApiClient {
    
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;
    private String baseUri;
    private String authToken;
    
    public ApiClient() {
        this.baseUri = config.getApiBaseUrl();
        initializeSpecs();
    }
    
    public ApiClient(String baseUri) {
        this.baseUri = baseUri;
        initializeSpecs();
    }
    
    /**
     * Initialize request and response specifications
     */
    private void initializeSpecs() {
        requestSpec = new RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
        
        responseSpec = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();
        
        logger.info("API Client initialized with base URI: {}", baseUri);
    }
    
    /**
     * Set authentication token
     */
    public ApiClient setAuthToken(String token) {
        this.authToken = token;
        return this;
    }
    
    /**
     * Set API key header
     */
    public ApiClient setApiKey(String apiKey) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader("X-API-Key", apiKey)
            .build();
        return this;
    }
    
    /**
     * Add custom header
     */
    public ApiClient addHeader(String name, String value) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader(name, value)
            .build();
        return this;
    }
    
    /**
     * Add multiple headers
     */
    public ApiClient addHeaders(Map<String, String> headers) {
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeaders(headers)
            .build();
        return this;
    }
    
    /**
     * Get request specification with auth if set
     */
    private RequestSpecification getRequestSpec() {
        if (authToken != null && !authToken.isEmpty()) {
            return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken);
        }
        return RestAssured.given().spec(requestSpec);
    }
    
    /**
     * Perform GET request
     */
    @Step("GET request to: {endpoint}")
    public Response get(String endpoint) {
        logger.info("Performing GET request to: {}", endpoint);
        return getRequestSpec()
            .when()
            .get(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform GET request with query parameters
     */
    @Step("GET request to: {endpoint} with params")
    public Response get(String endpoint, Map<String, ?> queryParams) {
        logger.info("Performing GET request to: {} with params: {}", endpoint, queryParams);
        return getRequestSpec()
            .queryParams(queryParams)
            .when()
            .get(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform POST request with body
     */
    @Step("POST request to: {endpoint}")
    public Response post(String endpoint, Object body) {
        logger.info("Performing POST request to: {}", endpoint);
        return getRequestSpec()
            .body(body)
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform POST request without body
     */
    @Step("POST request to: {endpoint}")
    public Response post(String endpoint) {
        logger.info("Performing POST request to: {}", endpoint);
        return getRequestSpec()
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform PUT request with body
     */
    @Step("PUT request to: {endpoint}")
    public Response put(String endpoint, Object body) {
        logger.info("Performing PUT request to: {}", endpoint);
        return getRequestSpec()
            .body(body)
            .when()
            .put(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform PATCH request with body
     */
    @Step("PATCH request to: {endpoint}")
    public Response patch(String endpoint, Object body) {
        logger.info("Performing PATCH request to: {}", endpoint);
        return getRequestSpec()
            .body(body)
            .when()
            .patch(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform DELETE request
     */
    @Step("DELETE request to: {endpoint}")
    public Response delete(String endpoint) {
        logger.info("Performing DELETE request to: {}", endpoint);
        return getRequestSpec()
            .when()
            .delete(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Perform DELETE request with body
     */
    @Step("DELETE request to: {endpoint}")
    public Response delete(String endpoint, Object body) {
        logger.info("Performing DELETE request to: {}", endpoint);
        return getRequestSpec()
            .body(body)
            .when()
            .delete(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Upload file
     */
    @Step("Upload file to: {endpoint}")
    public Response uploadFile(String endpoint, String filePath, String paramName) {
        logger.info("Uploading file to: {}", endpoint);
        return getRequestSpec()
            .contentType(ContentType.MULTIPART)
            .multiPart(paramName, new java.io.File(filePath))
            .when()
            .post(endpoint)
            .then()
            .spec(responseSpec)
            .extract()
            .response();
    }
    
    /**
     * Get base URI
     */
    public String getBaseUri() {
        return baseUri;
    }
    
    /**
     * Set base URI
     */
    public ApiClient setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        initializeSpecs();
        return this;
    }
}
