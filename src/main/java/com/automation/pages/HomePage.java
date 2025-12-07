package com.automation.pages;

import com.automation.base.BasePage;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Home Page - Page Object for the Home/Dashboard page after login.
 * Contains all home page related actions and verifications.
 */
public class HomePage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private static final String LOCATOR_FILE = "home-page.json";
    
    public HomePage() {
        super(LOCATOR_FILE);
    }
    
    /**
     * Get welcome message text
     */
    @Step("Get welcome message")
    public String getWelcomeMessage() {
        return getText("welcomeMessage");
    }
    
    /**
     * Check if welcome message is displayed
     */
    @Step("Check if welcome message is displayed")
    public boolean isWelcomeMessageDisplayed() {
        return isElementDisplayed("welcomeMessage");
    }
    
    /**
     * Click on user profile dropdown
     */
    @Step("Click user profile dropdown")
    public HomePage clickUserProfileDropdown() {
        click("userProfileDropdown");
        logger.info("Clicked user profile dropdown");
        return this;
    }
    
    /**
     * Click logout button
     */
    @Step("Click logout button")
    public LoginPage clickLogout() {
        click("logoutButton");
        logger.info("Clicked logout button");
        return new LoginPage();
    }
    
    /**
     * Perform logout
     */
    @Step("Perform logout")
    public LoginPage logout() {
        clickUserProfileDropdown();
        return clickLogout();
    }
    
    /**
     * Search for item
     */
    @Step("Search for: {searchTerm}")
    public HomePage search(String searchTerm) {
        enterText("searchInput", searchTerm);
        click("searchButton");
        logger.info("Searched for: {}", searchTerm);
        return this;
    }
    
    /**
     * Navigate to dashboard
     */
    @Step("Navigate to dashboard")
    public HomePage navigateToDashboard() {
        click("dashboardLink");
        logger.info("Navigated to dashboard");
        return this;
    }
    
    /**
     * Navigate to settings
     */
    @Step("Navigate to settings")
    public HomePage navigateToSettings() {
        click("settingsLink");
        logger.info("Navigated to settings");
        return this;
    }
    
    /**
     * Check if navigation menu is displayed
     */
    @Step("Check if navigation menu is displayed")
    public boolean isNavigationMenuDisplayed() {
        return isElementDisplayed("navigationMenu");
    }
    
    /**
     * Check if user is logged in (welcome message visible)
     */
    @Step("Verify user is logged in")
    public boolean isUserLoggedIn() {
        try {
            return isWelcomeMessageDisplayed() || isNavigationMenuDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get current page title
     */
    @Step("Get page title")
    public String getTitle() {
        return getPageTitle();
    }
    
    /**
     * Get current URL
     */
    @Step("Get current URL")
    public String getUrl() {
        return getCurrentUrl();
    }
}
