# Java Selenium Test Automation Framework

A comprehensive, enterprise-grade test automation framework built with **Java**, **Selenium WebDriver**, **TestNG**, and **REST Assured**. This framework supports both UI and API testing with robust reporting, CI/CD integration, and best practices implementation.

---

## Table of Contents

1. [Features](#features)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Prerequisites](#prerequisites)
5. [Installation & Setup](#installation--setup)
6. [Configuration](#configuration)
7. [Running Tests](#running-tests)
8. [JSON Locator System](#json-locator-system)
9. [Page Object Model](#page-object-model)
10. [API Testing](#api-testing)
11. [Reporting](#reporting)
12. [Screenshots](#screenshots)
13. [Browser Attachment Mode](#browser-attachment-mode)
14. [Email Notifications](#email-notifications)
15. [CI/CD Integration](#cicd-integration)
    - [GitHub Actions](#github-actions)
    - [Jenkins](#jenkins)
    - [Azure DevOps](#azure-devops)
    - [AWS CodePipeline](#aws-codepipeline)
16. [Allure Report Installation](#allure-report-installation)
17. [Best Practices](#best-practices)
18. [Troubleshooting](#troubleshooting)
19. [Contributing](#contributing)

---

## Features

- ✅ **Selenium WebDriver 4.x** - Latest Selenium with modern browser support
- ✅ **TestNG** - Powerful test framework with parallel execution
- ✅ **REST Assured** - Comprehensive API testing capabilities
- ✅ **Page Object Model (POM)** - Clean, maintainable test architecture
- ✅ **JSON-based Locators** - Simplified, externalized element locators
- ✅ **Multiple Browsers** - Chrome, Firefox, Edge, Safari support
- ✅ **Headless Mode** - Run tests without GUI
- ✅ **Parallel Execution** - Run tests concurrently for faster execution
- ✅ **Allure Reports** - Beautiful, detailed test reports
- ✅ **Extent Reports** - Alternative HTML reporting
- ✅ **Screenshot Capture** - Automatic screenshots on failure
- ✅ **Retry Mechanism** - Automatic retry for flaky tests
- ✅ **Environment Configuration** - Secure, environment-based settings
- ✅ **Browser Attachment** - Reuse browser sessions for faster debugging
- ✅ **Email Notifications** - Send test reports via email
- ✅ **CI/CD Ready** - GitHub Actions, Jenkins, Azure DevOps, AWS support
- ✅ **Logging** - Comprehensive logging with Log4j2

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Maven | 3.8+ | Build & Dependency Management |
| Selenium WebDriver | 4.15.0 | Browser Automation |
| TestNG | 7.8.0 | Test Framework |
| REST Assured | 5.3.2 | API Testing |
| WebDriverManager | 5.6.2 | Automatic Driver Management |
| Allure | 2.24.0 | Test Reporting |
| Extent Reports | 5.1.1 | HTML Reporting |
| Log4j2 | 2.21.1 | Logging |
| Jackson | 2.15.3 | JSON Processing |
| AssertJ | 3.24.2 | Fluent Assertions |

---

## Project Structure

```
Java-Selenium-Automation-Framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/automation/
│   │   │       ├── api/                    # API testing classes
│   │   │       │   ├── ApiClient.java      # REST API client
│   │   │       │   └── BaseApiTest.java    # Base class for API tests
│   │   │       ├── base/                   # Base classes
│   │   │       │   ├── BasePage.java       # Base page object
│   │   │       │   └── BaseTest.java       # Base test class
│   │   │       ├── config/                 # Configuration management
│   │   │       │   └── ConfigManager.java  # Configuration handler
│   │   │       ├── driver/                 # WebDriver management
│   │   │       │   └── DriverManager.java  # Driver factory
│   │   │       ├── listeners/              # TestNG listeners
│   │   │       │   ├── TestListener.java   # Test execution listener
│   │   │       │   └── RetryAnalyzer.java  # Retry failed tests
│   │   │       ├── locators/               # Locator management
│   │   │       │   └── LocatorManager.java # JSON locator parser
│   │   │       ├── pages/                  # Page objects
│   │   │       │   ├── LoginPage.java      # Login page object
│   │   │       │   └── HomePage.java       # Home page object
│   │   │       ├── reports/                # Reporting utilities
│   │   │       │   └── ExtentReportManager.java
│   │   │       └── utils/                  # Utility classes
│   │   │           ├── WaitUtils.java      # Wait utilities
│   │   │           ├── ScreenshotUtils.java# Screenshot utilities
│   │   │           └── EmailUtils.java     # Email utilities
│   │   └── resources/
│   │       ├── config/                     # Configuration files
│   │       │   ├── config.properties       # Default configuration
│   │       │   └── env.properties.template # Environment template
│   │       ├── locators/                   # JSON locator files
│   │       │   ├── login-page.json         # Login page locators
│   │       │   └── home-page.json          # Home page locators
│   │       ├── log4j2.xml                  # Logging configuration
│   │       └── allure.properties           # Allure configuration
│   └── test/
│       ├── java/
│       │   └── com/automation/tests/
│       │       ├── LoginTest.java          # Login test cases
│       │       └── api/
│       │           └── SampleApiTest.java  # API test cases
│       └── resources/
│           └── testng-suites/              # TestNG suite files
│               ├── testng.xml              # All tests
│               ├── smoke-tests.xml         # Smoke tests
│               ├── regression-tests.xml    # Regression tests
│               └── api-tests.xml           # API tests
├── .github/
│   └── workflows/
│       └── test-automation.yml             # GitHub Actions workflow
├── aws-codepipeline/
│   ├── buildspec.yml                       # AWS CodeBuild spec
│   └── cloudformation-template.yml         # AWS infrastructure
├── azure-pipelines.yml                     # Azure DevOps pipeline
├── Jenkinsfile                             # Jenkins pipeline
├── pom.xml                                 # Maven configuration
├── .gitignore                              # Git ignore rules
└── README.md                               # This file
```

---

## Prerequisites

Before you begin, ensure you have the following installed:

### 1. Java Development Kit (JDK) 11 or higher

**Windows:**
```bash
# Download from: https://adoptium.net/
# Or use Chocolatey:
choco install temurin11
```

**macOS:**
```bash
# Using Homebrew:
brew install openjdk@11

# Add to PATH:
echo 'export PATH="/usr/local/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-11-jdk
```

**Verify installation:**
```bash
java -version
# Should show: openjdk version "11.x.x"
```

### 2. Apache Maven 3.8+

**Windows:**
```bash
choco install maven
```

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt install maven
```

**Verify installation:**
```bash
mvn -version
# Should show: Apache Maven 3.x.x
```

### 3. Browser (Chrome recommended)

The framework uses WebDriverManager to automatically download browser drivers. Just ensure you have a browser installed:

- **Chrome**: https://www.google.com/chrome/
- **Firefox**: https://www.mozilla.org/firefox/
- **Edge**: https://www.microsoft.com/edge

---

## Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/amitbad/Java-Selenium-Automation-Framework.git
cd Java-Selenium-Automation-Framework
```

### Step 2: Install Dependencies

```bash
mvn clean install -DskipTests
```

### Step 3: Configure Environment

```bash
# Copy the environment template
cp src/main/resources/config/env.properties.template src/main/resources/config/env.properties

# Edit with your values
nano src/main/resources/config/env.properties  # or use any text editor
```

### Step 4: Verify Setup

```bash
# Run a quick test to verify everything works
mvn test -Dtest=SampleApiTest#testGetPosts
```

---

## Configuration

### Configuration Files

| File | Purpose |
|------|---------|
| `config.properties` | Default configuration (committed to repo) |
| `env.properties` | Environment-specific secrets (gitignored) |
| Environment Variables | Override any configuration at runtime |

### Configuration Hierarchy (Priority: High to Low)

1. **Environment Variables** - Highest priority
2. **env.properties** - Local overrides
3. **config.properties** - Default values

### Key Configuration Options

```properties
# Browser Configuration
browser=chrome                    # chrome, firefox, edge, safari
headless=false                    # true for CI/CD
browser.maximize=true
browser.implicit.wait=10
browser.explicit.wait=30

# Application URLs
base.url=https://your-app.com
api.base.url=https://api.your-app.com

# Screenshot Configuration
screenshot.on.failure=true
screenshot.path=target/screenshots

# Retry Configuration
retry.count=2

# Browser Attachment Mode
attach.browser=false
debug.port=9222
```

### Environment Variables

Set these environment variables for sensitive data:

```bash
# Linux/macOS
export BASE_URL="https://your-app.com"
export API_BASE_URL="https://api.your-app.com"
export TEST_USERNAME="your_username"
export TEST_PASSWORD="your_password"

# Windows (PowerShell)
$env:BASE_URL="https://your-app.com"
$env:API_BASE_URL="https://api.your-app.com"
$env:TEST_USERNAME="your_username"
$env:TEST_PASSWORD="your_password"
```

---

## Running Tests

### Basic Commands

```bash
# Run all tests
mvn clean test

# Run with specific profile
mvn clean test -Psmoke          # Smoke tests only
mvn clean test -Pregression     # Regression tests
mvn clean test -Papi            # API tests only
mvn clean test -Pall            # All tests (default)

# Run specific test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin

# Run tests with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true

# Run with multiple parameters
mvn clean test -Psmoke -Dbrowser=chrome -Dheadless=true
```

### Running with TestNG XML

```bash
# Run specific suite
mvn test -DsuiteXmlFile=src/test/resources/testng-suites/smoke-tests.xml
```

### Parallel Execution

Edit the TestNG XML file to configure parallel execution:

```xml
<suite name="Parallel Suite" parallel="classes" thread-count="3">
    <!-- tests -->
</suite>
```

---

## JSON Locator System

### Why JSON Locators?

- **Separation of Concerns** - Locators are separate from code
- **Easy Maintenance** - Update locators without changing code
- **Readability** - Clear, structured format
- **Reusability** - Share locators across tests

### JSON Locator Format

```json
{
  "pageName": "LoginPage",
  "description": "Locators for the Login Page",
  "locators": {
    "usernameInput": {
      "type": "id",
      "value": "username",
      "description": "Username input field"
    },
    "loginButton": {
      "type": "css",
      "value": "button[type='submit']",
      "description": "Login submit button"
    },
    "errorMessage": {
      "type": "xpath",
      "value": "//div[@class='error']",
      "description": "Error message element"
    }
  }
}
```

### Supported Locator Types

| Type | Description | Example |
|------|-------------|---------|
| `id` | Element ID | `"username"` |
| `css` | CSS Selector | `"button.submit"` |
| `xpath` | XPath | `"//input[@name='email']"` |
| `name` | Name attribute | `"email"` |
| `className` | Class name | `"btn-primary"` |
| `linkText` | Link text | `"Click Here"` |
| `partialLinkText` | Partial link text | `"Click"` |
| `tagName` | HTML tag | `"button"` |

### Using Locators in Page Objects

```java
public class LoginPage extends BasePage {
    
    public LoginPage() {
        super("login-page.json");  // Load locators from JSON
    }
    
    public void enterUsername(String username) {
        enterText("usernameInput", username);  // Use locator name
    }
    
    public void clickLogin() {
        click("loginButton");
    }
}
```

---

## Page Object Model

### Creating a New Page Object

1. Create JSON locator file in `src/main/resources/locators/`:

```json
// new-page.json
{
  "pageName": "NewPage",
  "locators": {
    "headerTitle": {
      "type": "css",
      "value": "h1.title",
      "description": "Page header title"
    }
  }
}
```

2. Create Page class:

```java
package com.automation.pages;

import com.automation.base.BasePage;
import io.qameta.allure.Step;

public class NewPage extends BasePage {
    
    public NewPage() {
        super("new-page.json");
    }
    
    @Step("Get header title")
    public String getHeaderTitle() {
        return getText("headerTitle");
    }
}
```

### Available BasePage Methods

| Method | Description |
|--------|-------------|
| `click(elementName)` | Click on element |
| `enterText(elementName, text)` | Enter text in input |
| `getText(elementName)` | Get element text |
| `getAttribute(elementName, attr)` | Get attribute value |
| `isElementDisplayed(elementName)` | Check if visible |
| `isElementEnabled(elementName)` | Check if enabled |
| `selectByVisibleText(elementName, text)` | Select dropdown |
| `hoverOver(elementName)` | Mouse hover |
| `scrollToElement(elementName)` | Scroll to element |
| `waitForElement(elementName)` | Wait for visibility |

---

## API Testing

### Using ApiClient

```java
public class MyApiTest extends BaseApiTest {
    
    @Test
    public void testGetUser() {
        Response response = apiClient.get("/users/1");
        
        assertStatusCode(response, 200);
        assertResponseContainsKey(response, "id");
        assertResponseKeyEquals(response, "name", "John Doe");
    }
    
    @Test
    public void testCreateUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Jane Doe");
        body.put("email", "jane@example.com");
        
        Response response = apiClient.post("/users", body);
        
        assertStatusCode(response, 201);
    }
}
```

### Available API Methods

| Method | Description |
|--------|-------------|
| `get(endpoint)` | GET request |
| `get(endpoint, params)` | GET with query params |
| `post(endpoint, body)` | POST request |
| `put(endpoint, body)` | PUT request |
| `patch(endpoint, body)` | PATCH request |
| `delete(endpoint)` | DELETE request |
| `setAuthToken(token)` | Set Bearer token |
| `addHeader(name, value)` | Add custom header |

---

## Reporting

### Allure Reports

Generate and view Allure reports:

```bash
# Run tests
mvn clean test

# Generate report
mvn allure:report

# Serve report (opens in browser)
mvn allure:serve
```

Report location: `target/site/allure-maven-plugin/index.html`

### Extent Reports

Extent reports are generated automatically after test execution.

Report location: `target/reports/TestReport_[timestamp].html`

---

## Screenshots

Screenshots are automatically captured on test failure when `screenshot.on.failure=true`.

### Manual Screenshot

```java
// In test or page object
takeScreenshot("custom_screenshot_name");
```

### Screenshot Location

`target/screenshots/[testname]_[timestamp].png`

---

## Browser Attachment Mode

Browser attachment allows you to reuse an existing browser session, which is useful for:
- Faster test development and debugging
- Maintaining login state across test runs
- Inspecting browser state after test failure

### Step 1: Start Browser with Debug Port

```bash
# macOS
/Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --remote-debugging-port=9222 --user-data-dir=/tmp/chrome_debug

# Windows
"C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir=C:\temp\chrome_debug

# Linux
google-chrome --remote-debugging-port=9222 --user-data-dir=/tmp/chrome_debug
```

### Step 2: Enable Attachment Mode

```properties
# In config.properties or env.properties
attach.browser=true
debug.port=9222
```

### Step 3: Run Tests

Tests will now attach to the existing browser instead of starting a new one.

```bash
mvn test -Dattach.browser=true
```

**Note:** The browser will remain open after tests complete for inspection.

---

## Email Notifications

### Configuration

Add email settings to `env.properties`:

```properties
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_password
EMAIL_FROM=your_email@gmail.com
EMAIL_TO=recipient@example.com
```

### Gmail Setup

1. Enable 2-Factor Authentication on your Google account
2. Generate an App Password:
   - Go to Google Account → Security → App passwords
   - Generate a new app password for "Mail"
   - Use this password in `EMAIL_PASSWORD`

### Sending Reports Programmatically

```java
// Send report after test execution
EmailUtils.sendTestCompletionEmail(
    passedCount, 
    failedCount, 
    skippedCount, 
    "target/reports/TestReport.html"
);
```

---

## CI/CD Integration

### GitHub Actions

The workflow is configured to run **manually only** by default.

#### Setup Steps:

1. **Add Secrets** in GitHub Repository:
   - Go to Repository → Settings → Secrets and variables → Actions
   - Add the following secrets:
     - `BASE_URL` - Your application URL
     - `API_BASE_URL` - Your API URL
     - `TEST_USERNAME` - Test user username
     - `TEST_PASSWORD` - Test user password

2. **Run Workflow Manually**:
   - Go to Actions tab
   - Select "Test Automation Pipeline"
   - Click "Run workflow"
   - Select options (test suite, browser, environment)
   - Click "Run workflow"

3. **Enable Automatic Runs** (Optional):
   - Edit `.github/workflows/test-automation.yml`
   - Uncomment the `push` and `pull_request` triggers

#### View Results:
- Test results appear in the Actions tab
- Download artifacts (reports, screenshots) from the workflow run

---

### Jenkins

#### Prerequisites:
- Jenkins with Pipeline plugin
- JDK 11 configured as "JDK11"
- Maven 3 configured as "Maven3"
- Allure plugin (optional, for reports)

#### Setup Steps:

1. **Create Pipeline Job**:
   - New Item → Pipeline
   - Name: "Test-Automation"

2. **Configure Pipeline**:
   - Pipeline → Definition: "Pipeline script from SCM"
   - SCM: Git
   - Repository URL: Your repo URL
   - Script Path: `Jenkinsfile`

3. **Add Credentials**:
   - Manage Jenkins → Credentials
   - Add credentials with IDs:
     - `base-url`
     - `api-base-url`
     - `test-username`
     - `test-password`

4. **Configure Tools**:
   - Manage Jenkins → Global Tool Configuration
   - Add JDK installation named "JDK11"
   - Add Maven installation named "Maven3"

5. **Run Pipeline**:
   - Open the job
   - Click "Build with Parameters"
   - Select test suite, browser, environment
   - Click "Build"

#### Install Allure Plugin:
1. Manage Jenkins → Plugins → Available
2. Search "Allure"
3. Install "Allure Jenkins Plugin"
4. Configure in Global Tool Configuration

---

### Azure DevOps

#### Setup Steps:

1. **Create Pipeline**:
   - Pipelines → New Pipeline
   - Select your repository
   - Choose "Existing Azure Pipelines YAML file"
   - Select `/azure-pipelines.yml`

2. **Add Variables**:
   - Edit Pipeline → Variables
   - Add variables (mark as secret where needed):
     - `BASE_URL`
     - `API_BASE_URL`
     - `TEST_USERNAME`
     - `TEST_PASSWORD`

3. **Run Pipeline**:
   - Click "Run pipeline"
   - Select parameters
   - Click "Run"

#### View Results:
- Test results in "Tests" tab
- Download artifacts from "Summary" tab

---

### AWS CodePipeline

#### Setup Steps:

1. **Create Secrets in AWS Secrets Manager**:
   ```bash
   aws secretsmanager create-secret \
     --name test-automation/secrets \
     --secret-string '{"BASE_URL":"https://your-app.com","API_BASE_URL":"https://api.your-app.com","TEST_USERNAME":"user","TEST_PASSWORD":"pass"}'
   ```

2. **Deploy CloudFormation Stack**:
   ```bash
   aws cloudformation create-stack \
     --stack-name test-automation-pipeline \
     --template-body file://aws-codepipeline/cloudformation-template.yml \
     --parameters \
       ParameterKey=GitHubOwner,ParameterValue=your-username \
       ParameterKey=GitHubRepo,ParameterValue=your-repo \
       ParameterKey=GitHubBranch,ParameterValue=main \
       ParameterKey=GitHubToken,ParameterValue=your-github-token \
     --capabilities CAPABILITY_NAMED_IAM
   ```

3. **Manual Trigger**:
   - Go to AWS CodePipeline console
   - Select your pipeline
   - Click "Release change"

#### Alternative: Direct CodeBuild

```bash
# Create CodeBuild project manually and run:
aws codebuild start-build --project-name test-automation-build
```

---

## Allure Report Installation

### Windows

```bash
# Using Scoop
scoop install allure

# Or using Chocolatey
choco install allure
```

### macOS

```bash
brew install allure
```

### Linux

```bash
# Download and extract
wget https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.tgz
tar -xzf allure-2.24.0.tgz
sudo mv allure-2.24.0 /opt/allure

# Add to PATH
echo 'export PATH="/opt/allure/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

### Verify Installation

```bash
allure --version
```

### Generate Report

```bash
# After running tests
allure generate target/allure-results --clean -o target/allure-report

# Open report in browser
allure open target/allure-report
```

---

## Best Practices

### 1. Locator Strategy
- Prefer `id` > `css` > `xpath`
- Use meaningful, stable locators
- Avoid dynamic IDs or classes

### 2. Test Independence
- Each test should be independent
- Don't rely on test execution order
- Clean up test data after tests

### 3. Wait Strategy
- Use explicit waits over implicit waits
- Avoid `Thread.sleep()`
- Use appropriate wait conditions

### 4. Configuration
- Never commit sensitive data
- Use environment variables for secrets
- Keep configuration externalized

### 5. Reporting
- Add meaningful test descriptions
- Use Allure annotations (`@Step`, `@Description`)
- Capture screenshots on failure

### 6. Code Organization
- Follow Page Object Model
- Keep tests focused and small
- Use meaningful method names

---

## Troubleshooting

### Common Issues

#### 1. WebDriver not found
```
Solution: WebDriverManager handles this automatically. 
Ensure you have internet connectivity on first run.
```

#### 2. Element not found
```
Solution: 
- Check if locator is correct
- Add explicit wait before interaction
- Verify element is in viewport
```

#### 3. Tests fail in headless mode
```
Solution:
- Set window size: --window-size=1920,1080
- Check for elements that behave differently in headless
```

#### 4. Allure report not generating
```
Solution:
mvn clean test
mvn allure:report
# Check target/allure-results has files
```

#### 5. Email not sending
```
Solution:
- Verify SMTP settings
- For Gmail, use App Password
- Check firewall/network restrictions
```

### Debug Mode

Enable debug logging:
```properties
# In log4j2.xml, change level to DEBUG
<Logger name="com.automation" level="DEBUG">
```

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## Support

For issues and questions:
- Create an issue in the GitHub repository
- Check existing issues for solutions

---

**Happy Testing!** 🚀
