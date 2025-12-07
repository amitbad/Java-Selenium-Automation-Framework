package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.listeners.RetryAnalyzer;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Login Test - Test class for login functionality.
 * Demonstrates UI testing with Page Object Model.
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {
    
    private LoginPage loginPage;
    
    @BeforeMethod(alwaysRun = true)
    @Override
    public void beforeMethod() {
        super.beforeMethod();
        loginPage = new LoginPage();
    }
    
    @Test(description = "Verify successful login with valid credentials", 
          groups = {"smoke", "login"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.BLOCKER)
    @Story("User Login")
    @Description("Test to verify that a user can login successfully with valid credentials")
    public void testSuccessfulLogin() {
        logger.info("Starting successful login test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Verify login form is displayed
        assertThat(loginPage.isLoginFormDisplayed())
            .as("Login form should be displayed")
            .isTrue();
        
        // Perform login
        HomePage homePage = loginPage.loginWithTestCredentials();
        
        // Verify successful login
        assertThat(homePage.isUserLoggedIn())
            .as("User should be logged in")
            .isTrue();
        
        logger.info("Successful login test completed");
    }
    
    @Test(description = "Verify login fails with invalid credentials",
          groups = {"smoke", "login", "negative"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Login")
    @Description("Test to verify that login fails with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        logger.info("Starting invalid credentials login test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Attempt login with invalid credentials
        loginPage.enterUsername("invalid_user");
        loginPage.enterPassword("invalid_password");
        loginPage.clickLoginButton();
        
        // Verify error message is displayed
        assertThat(loginPage.isErrorMessageDisplayed())
            .as("Error message should be displayed")
            .isTrue();
        
        logger.info("Invalid credentials login test completed");
    }
    
    @Test(description = "Verify login button is disabled with empty fields",
          groups = {"regression", "login"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("User Login")
    @Description("Test to verify login button behavior with empty fields")
    public void testLoginButtonWithEmptyFields() {
        logger.info("Starting empty fields login test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Clear any pre-filled values
        loginPage.clearUsername();
        loginPage.clearPassword();
        
        // Verify login form is displayed
        assertThat(loginPage.isLoginFormDisplayed())
            .as("Login form should be displayed")
            .isTrue();
        
        logger.info("Empty fields login test completed");
    }
    
    @Test(description = "Verify remember me functionality",
          groups = {"regression", "login"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.MINOR)
    @Story("User Login")
    @Description("Test to verify remember me checkbox functionality")
    public void testRememberMeCheckbox() {
        logger.info("Starting remember me checkbox test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Check remember me
        loginPage.checkRememberMe();
        
        // Verify checkbox is selected
        assertThat(loginPage.isRememberMeSelected())
            .as("Remember me checkbox should be selected")
            .isTrue();
        
        logger.info("Remember me checkbox test completed");
    }
    
    @Test(description = "Verify forgot password link",
          groups = {"regression", "login"},
          retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Password Recovery")
    @Description("Test to verify forgot password link navigation")
    public void testForgotPasswordLink() {
        logger.info("Starting forgot password link test");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Click forgot password link
        loginPage.clickForgotPassword();
        
        // Verify navigation (URL should change)
        String currentUrl = loginPage.getUrl();
        assertThat(currentUrl)
            .as("Should navigate to forgot password page")
            .contains("forgot");
        
        logger.info("Forgot password link test completed");
    }
}
