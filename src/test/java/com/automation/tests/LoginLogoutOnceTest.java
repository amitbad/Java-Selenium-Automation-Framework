package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.driver.DriverManager;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Login Logout Once Test - Demonstrates executing login/logout only once for a test class.
 * This pattern is useful when:
 * - Login is time-consuming
 * - Multiple tests need to run in a logged-in state
 * - You want to save execution time
 * 
 * The login happens in @BeforeClass (once per class)
 * The logout happens in @AfterClass (once per class)
 * Individual tests run without re-logging in
 */
@Epic("Authentication")
@Feature("Session Management")
public class LoginLogoutOnceTest extends BaseTest {
    
    private LoginPage loginPage;
    private HomePage homePage;
    
    /**
     * Setup - Login once before all tests in this class
     * This overrides the parent @BeforeMethod to prevent per-test driver initialization
     */
    @BeforeClass(alwaysRun = true)
    @Override
    public void beforeClass() {
        logger.info("========================================");
        logger.info("LOGIN ONCE - Starting test class setup");
        logger.info("========================================");
        
        // Initialize driver once for the entire class
        DriverManager.initDriver();
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty() && !baseUrl.contains("${")) {
            getDriver().get(baseUrl);
        }
        
        // Perform login once
        loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        homePage = loginPage.loginWithTestCredentials();
        
        logger.info("Login completed - Session established for all tests");
    }
    
    /**
     * Override parent beforeMethod to skip driver initialization
     * (driver is already initialized in beforeClass)
     */
    @BeforeMethod(alwaysRun = true)
    @Override
    public void beforeMethod() {
        // Don't initialize driver - it's already done in beforeClass
        logger.info("Starting test method (using existing session)");
    }
    
    /**
     * Override parent afterMethod to skip driver quit
     * (driver will be quit in afterClass)
     */
    @AfterMethod(alwaysRun = true)
    @Override
    public void afterMethod(org.testng.ITestResult result) {
        // Don't quit driver - keep session for next test
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            logger.error("Test FAILED: {}", result.getName());
        } else if (result.getStatus() == org.testng.ITestResult.SUCCESS) {
            logger.info("Test PASSED: {}", result.getName());
        }
    }
    
    /**
     * Teardown - Logout and quit driver once after all tests
     */
    @AfterClass(alwaysRun = true)
    @Override
    public void afterClass() {
        logger.info("========================================");
        logger.info("LOGOUT ONCE - Cleaning up test class");
        logger.info("========================================");
        
        try {
            // Perform logout
            if (homePage != null) {
                homePage.logout();
                logger.info("Logout completed");
            }
        } catch (Exception e) {
            logger.warn("Logout failed or already logged out: {}", e.getMessage());
        } finally {
            // Quit driver
            DriverManager.quitDriver();
            logger.info("Driver quit - Session ended");
        }
    }
    
    // ==================== Test Methods ====================
    // All these tests run with the same logged-in session
    
    @Test(description = "Verify user is logged in",
          groups = {"smoke", "session"},
          priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Session Verification")
    @Description("Verify that user session is active after login")
    public void testUserIsLoggedIn() {
        logger.info("Verifying user is logged in");
        
        assertThat(homePage.isUserLoggedIn())
            .as("User should be logged in")
            .isTrue();
    }
    
    @Test(description = "Verify welcome message is displayed",
          groups = {"smoke", "session"},
          priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Story("Session Verification")
    @Description("Verify welcome message is displayed for logged-in user")
    public void testWelcomeMessageDisplayed() {
        logger.info("Verifying welcome message");
        
        assertThat(homePage.isWelcomeMessageDisplayed())
            .as("Welcome message should be displayed")
            .isTrue();
    }
    
    @Test(description = "Verify navigation menu is accessible",
          groups = {"regression", "session"},
          priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation")
    @Description("Verify navigation menu is accessible for logged-in user")
    public void testNavigationMenuAccessible() {
        logger.info("Verifying navigation menu");
        
        assertThat(homePage.isNavigationMenuDisplayed())
            .as("Navigation menu should be displayed")
            .isTrue();
    }
    
    @Test(description = "Verify dashboard navigation",
          groups = {"regression", "session"},
          priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation")
    @Description("Verify user can navigate to dashboard")
    public void testDashboardNavigation() {
        logger.info("Testing dashboard navigation");
        
        homePage.navigateToDashboard();
        
        String currentUrl = homePage.getUrl();
        assertThat(currentUrl)
            .as("Should be on dashboard page")
            .contains("dashboard");
    }
    
    @Test(description = "Verify search functionality",
          groups = {"regression", "session"},
          priority = 5)
    @Severity(SeverityLevel.MINOR)
    @Story("Search")
    @Description("Verify search functionality works for logged-in user")
    public void testSearchFunctionality() {
        logger.info("Testing search functionality");
        
        homePage.search("test query");
        
        // Verify search was performed (URL or results)
        String currentUrl = homePage.getUrl();
        assertThat(currentUrl)
            .as("Search should be performed")
            .containsIgnoringCase("search");
    }
}
