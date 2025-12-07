package com.automation.pages;

import com.automation.base.BasePage;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Login Page - Page Object for the Login page.
 * Contains all login-related actions and verifications.
 */
public class LoginPage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private static final String LOCATOR_FILE = "login-page.json";
    
    public LoginPage() {
        super(LOCATOR_FILE);
    }
    
    /**
     * Navigate to login page
     */
    @Step("Navigate to login page")
    public LoginPage navigateToLoginPage() {
        String loginUrl = config.getBaseUrl() + "/login";
        navigateTo(loginUrl);
        logger.info("Navigated to login page");
        return this;
    }
    
    /**
     * Enter username
     */
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        enterText("usernameInput", username);
        logger.info("Entered username");
        return this;
    }
    
    /**
     * Enter password
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        enterText("passwordInput", password);
        logger.info("Entered password");
        return this;
    }
    
    /**
     * Click login button
     */
    @Step("Click login button")
    public void clickLoginButton() {
        click("loginButton");
        logger.info("Clicked login button");
    }
    
    /**
     * Check remember me checkbox
     */
    @Step("Check remember me checkbox")
    public LoginPage checkRememberMe() {
        if (!isElementSelected("rememberMeCheckbox")) {
            click("rememberMeCheckbox");
            logger.info("Checked remember me checkbox");
        }
        return this;
    }
    
    /**
     * Click forgot password link
     */
    @Step("Click forgot password link")
    public void clickForgotPassword() {
        click("forgotPasswordLink");
        logger.info("Clicked forgot password link");
    }
    
    /**
     * Perform login with credentials
     */
    @Step("Login with username: {username}")
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Performed login");
        return new HomePage();
    }
    
    /**
     * Perform login with configured test credentials
     */
    @Step("Login with test credentials")
    public HomePage loginWithTestCredentials() {
        String username = config.getTestUsername();
        String password = config.getTestPassword();
        return login(username, password);
    }
    
    /**
     * Get error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        return getText("errorMessage");
    }
    
    /**
     * Check if error message is displayed
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed("errorMessage");
    }
    
    /**
     * Check if login form is displayed
     */
    @Step("Check if login form is displayed")
    public boolean isLoginFormDisplayed() {
        return isElementDisplayed("loginForm");
    }
    
    /**
     * Check if login button is enabled
     */
    @Step("Check if login button is enabled")
    public boolean isLoginButtonEnabled() {
        return isElementEnabled("loginButton");
    }
    
    /**
     * Clear username field
     */
    @Step("Clear username field")
    public LoginPage clearUsername() {
        findElement("usernameInput").clear();
        return this;
    }
    
    /**
     * Clear password field
     */
    @Step("Clear password field")
    public LoginPage clearPassword() {
        findElement("passwordInput").clear();
        return this;
    }
    
    /**
     * Get username field value
     */
    @Step("Get username field value")
    public String getUsernameValue() {
        return getAttribute("usernameInput", "value");
    }
    
    /**
     * Get password field value
     */
    @Step("Get password field value")
    public String getPasswordValue() {
        return getAttribute("passwordInput", "value");
    }
    
    /**
     * Check if remember me checkbox is selected
     */
    @Step("Check if remember me is selected")
    public boolean isRememberMeSelected() {
        return isElementSelected("rememberMeCheckbox");
    }
    
    /**
     * Get current page URL
     */
    @Step("Get current URL")
    public String getUrl() {
        return getCurrentUrl();
    }
}
