# Banking Automation Framework - Detailed Technical Explanation

## For New Automation Testers

This guide explains the complete technical setup, how everything connects, and what happens at each step.

---

## TABLE OF CONTENTS
1. TestNG Configuration & How It Works
2. Cucumber Setup & BDD Integration
3. Maven Build Lifecycle
4. Complete mvn clean test Execution Flow
5. Purpose of Each Key File
6. How Tests Are Called & Executed

---

## PART 1: TestNG CONFIGURATION & HOW IT WORKS

### What is TestNG?

TestNG is a testing framework that:
- Organizes tests into methods marked with `@Test`
- Provides setup/teardown methods (`@BeforeMethod`, `@AfterMethod`)
- Groups tests in suites (defined in XML)
- Generates test reports
- Supports parallel execution
- Replaces older JUnit framework

### TestNG Configuration File: testng.xml

**Location**: `C:\Users\balak\DTAutomstionFramework\testng.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Banking Automation Framework Test Suite" parallel="false">
    <!-- This defines the test suite name and parallel execution setting -->

    <test name="Banking Framework Verification Tests - PASSING">
        <!-- Test group 1: Framework validation tests -->
        <classes>
            <class name="tests.BankingFrameworkVerificationTest"/>
            <!-- Points to class: src/test/java/tests/BankingFrameworkVerificationTest.java -->
        </classes>
    </test>

    <test name="Banking Login Configuration Tests">
        <!-- Test group 2: Login configuration tests -->
        <classes>
            <class name="tests.BankingLoginTest"/>
            <!-- Points to class: src/test/java/tests/BankingLoginTest.java -->
        </classes>
    </test>

    <test name="ParaBank UI Tests - Real Application Testing">
        <!-- Test group 3: Real ParaBank app tests -->
        <classes>
            <class name="tests.ParaBankUITest"/>
            <!-- Points to class: src/test/java/tests/ParaBankUITest.java -->
        </classes>
    </test>

</suite>
```

### How TestNG Reads testng.xml

```
User runs: mvn clean test
    ↓
Maven Surefire Plugin kicks in (specified in pom.xml)
    ↓
Surefire Plugin reads: testng.xml
    ↓
Finds 3 test suites defined
    ↓
For each suite:
  ├─ Finds the test class
  ├─ Discovers all @Test methods in that class
  ├─ Executes @BeforeMethod (setup)
  ├─ Executes @Test method
  ├─ Executes @AfterMethod (teardown)
```

### TestNG Annotations Explained

```java
@BeforeMethod
public void setup() {
    // Runs BEFORE each @Test method
    // Called 30 times (once before each test)
    // Purpose: Initialize browser, navigate to website
}

@Test
public void testName() {
    // The actual test method
    // Marked with @Test annotation so TestNG recognizes it
    // Contains assertions (Assert.assertEquals, etc.)
}

@AfterMethod
public void teardown() {
    // Runs AFTER each @Test method
    // Called 30 times (once after each test)
    // Purpose: Close browser, clean resources
}
```

### Example: How One Test Executes

```java
public class ParaBankUITest extends BaseTest {
    // BaseTest contains @BeforeMethod and @AfterMethod
    
    @Test(description = "Test 1: Verify ParaBank homepage loads", priority = 1)
    public void testParaBankHomepageLoads() {
        // Test code here
    }
}
```

**Execution sequence**:

```
TestNG finds: public void testParaBankHomepageLoads()
    ↓
TestNG looks for @BeforeMethod in class or parent (BaseTest)
    ↓
Executes: BaseTest.setup()
    ├─ DriverFactory.initDriver("chrome")     [Browser starts]
    ├─ DriverManager.getDriver()               [Get driver]
    └─ driver.get("https://parabank.parasoft.com/")  [Navigate]
    ↓
Executes: testParaBankHomepageLoads() method
    ├─ driver.getTitle()
    ├─ Assert.assertNotNull(title)
    ├─ ScreenshotUtil.takeScreenshot()
    └─ [Test completes]
    ↓
TestNG looks for @AfterMethod
    ↓
Executes: BaseTest.teardown()
    ├─ driver.quit()                          [Browser closes]
    └─ DriverManager.remove()                 [Clean ThreadLocal]
    ↓
[Test marked as PASSED or FAILED]
```

---

## PART 2: CUCUMBER SETUP & BDD INTEGRATION

### What is Cucumber?

Cucumber is a BDD (Behavior-Driven Development) framework that:
- Writes tests in plain English (Gherkin language)
- Business people can read and understand tests
- Maps English steps to Java code
- Works alongside TestNG

### Cucumber Files Structure

```
src/test/resources/
├── features/                          ← Gherkin feature files
│   ├── BankingLogin.feature
│   ├── BankingDashboard.feature
│   └── BankingTransfer.feature
│
└── config/
    └── config.properties
```

### Example: Feature File

**File**: `src/test/resources/features/BankingLogin.feature`

```gherkin
Feature: Banking Login Functionality
  # This is what the feature does (human readable)

  Scenario: Successful login with valid credentials
    # Scenario = one test case in plain English
    
    Given user is on the banking login page
    # GIVEN = precondition (what state we start in)
    
    When user logs in with username "admin@banking.com" and password "admin"
    # WHEN = action (what the user does)
    
    Then user should be logged in successfully
    # THEN = expected result (what should happen)

  Scenario: Failed login with invalid password
    Given user is on the banking login page
    When user enters username "admin@banking.com"
    And user enters password "wrongPassword"
    And user clicks the login button
    Then login should fail with an error message
```

### How Cucumber Maps to Java

**Feature File Step**:
```gherkin
Given user is on the banking login page
```

**Java Step Definition**:
```java
// File: src/test/java/stepdefinitions/BankingLoginSteps.java

@Given("user is on the banking login page")
public void userIsOnLoginPage() {
    // Java code that executes when feature file has this step
    loginPage = new BankingLoginPage(DriverManager.getDriver());
    loginPage.waitForLoginPageToLoad();
}
```

**Cucumber maps**:
```
Feature file text (English)
         ↓
Regex pattern in @Given annotation
         ↓
Calls Java method
         ↓
Java method interacts with page objects
         ↓
Assertions verify results
```

### Complete Feature to Java Flow

```
Feature File (BankingLogin.feature)
    ↓
Scenario: "Successful login with valid credentials"
    ↓
Step 1: Given user is on the banking login page
    ↓
Cucumber finds: @Given("user is on the banking login page")
    ↓
Executes: BankingLoginSteps.userIsOnLoginPage()
    ├─ new BankingLoginPage(driver)
    └─ loginPage.waitForLoginPageToLoad()
    ↓
Step 2: When user logs in with username "admin@banking.com" and password "admin"
    ↓
Cucumber finds: @When("user logs in with username {string} and password {string}")
    ↓
Executes: BankingLoginSteps.userLogsIn("admin@banking.com", "admin")
    ├─ loginPage.enterUsername("admin@banking.com")
    ├─ loginPage.enterPassword("admin")
    └─ loginPage.clickLoginButton()
    ↓
Step 3: Then user should be logged in successfully
    ↓
Cucumber finds: @Then("user should be logged in successfully")
    ↓
Executes: BankingLoginSteps.userShouldBeLoggedInSuccessfully()
    ├─ Assert.assertTrue(dashboardPage.isVisible())
    └─ Assert.assertTrue(pageTitle.contains("Dashboard"))
    ↓
[Scenario PASSED or FAILED]
```

### Cucumber Hooks

**File**: `src/test/java/stepdefinitions/Hooks.java`

```java
public class Hooks {
    
    @Before
    public void setup() {
        // Runs BEFORE each Cucumber scenario
        // Similar to TestNG @BeforeMethod
        DriverFactory.initDriver("chrome");
        DriverManager.getDriver().get(ConfigReader.get("baseURL"));
    }
    
    @After
    public void teardown() {
        // Runs AFTER each Cucumber scenario
        // Similar to TestNG @AfterMethod
        DriverManager.quit();
    }
}
```

### Cucumber Test Runner

**File**: `src/test/java/runners/BankingTestRunner.java`

```java
@CucumberOptions(
    features = "src/test/resources/features",
    // ↑ Where to find .feature files
    
    glue = "stepdefinitions",
    // ↑ Where to find step definitions (@Given, @When, @Then)
    
    plugin = {"pretty", "html:target/cucumber-report.html"}
    // ↑ Report generation
)
public class BankingTestRunner extends AbstractTestNGCucumberTests {
    // This class runs all Cucumber scenarios
}
```

### How Cucumber Integrates with TestNG

```
mvn clean test
    ↓
TestNG finds BankingTestRunner class
    ↓
BankingTestRunner extends AbstractTestNGCucumberTests
    ↓
Cucumber scans: src/test/resources/features/
    ↓
Finds: BankingLogin.feature
    ↓
For each Scenario in feature file:
    ├─ @Before Hooks.setup()          [Browser starts]
    ├─ Execute each Step               [Java methods]
    ├─ @After Hooks.teardown()        [Browser closes]
    └─ Record result
    ↓
Generate: target/cucumber-report.html
```

---

## PART 3: MAVEN BUILD LIFECYCLE

### What is Maven?

Maven is a build automation tool that:
- Manages dependencies (downloads from internet)
- Compiles Java code
- Runs tests
- Generates reports
- Follows standardized project structure

### Maven Lifecycle Phases

When you run `mvn clean test`, Maven executes these phases in order:

```
clean      → Delete old builds
validate   → Check project structure
compile    → Compile main source code
test       → Run tests
```

### pom.xml: The Maven Configuration File

**Location**: `C:\Users\balak\DTAutomstionFramework\pom.xml`

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <!-- Tells Maven this is a POM 4.0 file -->

    <groupId>com.banking.automation</groupId>
    <!-- Company identifier (like package name) -->

    <artifactId>banking-automation-framework</artifactId>
    <!-- Project name -->

    <version>1.0.0</version>
    <!-- Current version -->

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <!-- Use Java 11 -->
    </properties>

    <dependencies>
        <!-- Libraries this project needs -->

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.18.1</version>
            <!-- Downloads Selenium 4.18.1 from Maven Central -->
        </dependency>

        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.9.0</version>
            <!-- Downloads TestNG 7.9.0 -->
        </dependency>

        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>7.14.0</version>
            <!-- Downloads Cucumber 7.14.0 -->
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <!-- Runs tests (reads testng.xml) -->
            </plugin>
        </plugins>
    </build>
</project>
```

### Maven Directory Structure

```
Project/
├── pom.xml                    ← Maven configuration
├── src/
│   ├── main/java/            ← Production code
│   │   ├── base/
│   │   ├── driver/
│   │   ├── pages/
│   │   └── utils/
│   │
│   ├── test/java/            ← Test code
│   │   ├── tests/
│   │   ├── stepdefinitions/
│   │   ├── runners/
│   │   └── api/
│   │
│   └── test/resources/       ← Test data & config
│       ├── features/
│       └── config/
│
└── target/                    ← Build output
    ├── classes/               ← Compiled main code
    ├── test-classes/          ← Compiled test code
    └── surefire-reports/      ← Test reports
```

---

## PART 4: COMPLETE mvn clean test EXECUTION FLOW

### Step-by-Step What Happens

```
1. USER TYPES COMMAND
   $ mvn clean test
        ↓

2. MAVEN CLEAN PHASE (maven-clean-plugin)
   ├─ Deletes: target/ directory
   ├─ Deletes: All old compiled code
   └─ Deletes: Old test results
        ↓

3. MAVEN VALIDATE PHASE
   ├─ Checks: pom.xml is valid XML
   └─ Checks: Project structure is correct
        ↓

4. MAVEN COMPILE PHASE (maven-compiler-plugin)
   ├─ Reads: pom.xml
   ├─ Downloads: All dependencies
   │  ├─ Selenium 4.18.1
   │  ├─ TestNG 7.9.0
   │  ├─ Cucumber 7.14.0
   │  ├─ WebDriverManager 5.7.0
   │  └─ Others...
   │  [Downloaded to ~/.m2/repository/]
   │
   ├─ Compiles: src/main/java/*.java
   │  ├─ base/BaseTest.java
   │  ├─ driver/DriverManager.java
   │  ├─ driver/DriverFactory.java
   │  ├─ pages/*.java
   │  └─ utils/*.java
   │
   └─ Output: target/classes/
      ├─ base/BaseTest.class
      ├─ driver/DriverManager.class
      ├─ driver/DriverFactory.class
      └─ [All compiled .class files]
        ↓

5. MAVEN TEST RESOURCES PHASE
   ├─ Copies: src/test/resources/
   │  ├─ config/config.properties
   │  └─ features/*.feature
   │
   └─ To: target/test-classes/
        ↓

6. MAVEN TEST COMPILE PHASE (maven-compiler-plugin)
   ├─ Compiles: src/test/java/*.java
   │  ├─ tests/BankingFrameworkVerificationTest.java
   │  ├─ tests/BankingLoginTest.java
   │  ├─ tests/ParaBankUITest.java (12 REAL TESTS)
   │  ├─ stepdefinitions/*.java
   │  ├─ runners/BankingTestRunner.java
   │  └─ api/*.java
   │
   └─ Output: target/test-classes/
        ↓

7. MAVEN TEST PHASE (maven-surefire-plugin) ⭐ THE BIG ONE
   ├─ Read: testng.xml
   │
   ├─ SUITE 1: BankingFrameworkVerificationTest
   │  ├─ For each of 12 @Test methods:
   │  │  ├─ @BeforeMethod BaseTest.setup()
   │  │  │  ├─ DriverFactory.initDriver("chrome")
   │  │  │  │  └─ WebDriverManager.chromedriver().setup()
   │  │  │  │     └─ Download ChromeDriver 146 (if not cached)
   │  │  │  ├─ new ChromeDriver(options)
   │  │  │  └─ driver.get("https://parabank.parasoft.com/")
   │  │  ├─ Execute: Test method
   │  │  │  ├─ Class.forName("pages.BankingLoginPage")
   │  │  │  ├─ ConfigReader.get("baseURL")
   │  │  │  └─ Assert.assertNotNull(...)
   │  │  └─ @AfterMethod BaseTest.teardown()
   │  │     ├─ driver.quit()
   │  │     └─ DriverManager.remove()
   │
   ├─ SUITE 2: BankingLoginTest
   │  ├─ For each of 6 @Test methods:
   │  │  ├─ @BeforeMethod
   │  │  ├─ Test execution
   │  │  └─ @AfterMethod
   │
   └─ SUITE 3: ParaBankUITest ⭐ REAL TESTS
      ├─ For each of 12 @Test methods:
      │  ├─ @BeforeMethod BaseTest.setup()
      │  │  ├─ Chrome opens
      │  │  └─ Navigate to ParaBank homepage
      │  │     └─ ~5-7 seconds
      │  ├─ Test execution
      │  │  ├─ Test 1: Verify homepage loads ✅
      │  │  ├─ Test 2: Login form accessible ✅
      │  │  ├─ Test 3: Register flow ✅
      │  │  ├─ Test 4: Invalid credentials ✅
      │  │  ├─ Test 5: Empty username ✅
      │  │  ├─ Test 6: Empty password ✅
      │  │  ├─ Test 7: Page elements ✅
      │  │  ├─ Test 8: Password field type ✅
      │  │  ├─ Test 9: Forgot password ✅
      │  │  ├─ Test 10: Responsiveness ✅
      │  │  ├─ Test 11: Form action ✅
      │  │  └─ Test 12: Page content ✅
      │  ├─ ScreenshotUtil.takeScreenshot()
      │  │  └─ Saves to: test-output/screenshots/
      │  └─ @AfterMethod BaseTest.teardown()
      │     ├─ driver.quit()
      │     └─ DriverManager.remove()
   ↓

8. REPORT GENERATION
   ├─ Generate: target/surefire-reports/
   │  ├─ TestSuite.xml
   │  ├─ TestSuite.txt
   │  └─ index.html
   │
   └─ Generate: allure-results/
      ├─ Multiple *.json files
      └─ Container information
        ↓

9. FINAL RESULT
   ✅ Tests run: 30
   ✅ Failures: 0
   ✅ Errors: 0
   ✅ Skipped: 0
   ✅ BUILD SUCCESS
   
   Total Time: 2 minutes 44 seconds
```

---

## PART 5: PURPOSE OF EACH KEY FILE

### Core Framework Files

#### **src/main/java/base/BaseTest.java**

**Purpose**: Base class for ALL tests

**What it does**:
```java
public class BaseTest {
    @BeforeMethod
    public void setup() {
        DriverFactory.initDriver(ConfigReader.get("browser"));
        DriverManager.getDriver().get(ConfigReader.get("baseURL"));
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quit();
    }
}
```

**How it's used**:
```java
public class ParaBankUITest extends BaseTest {
    // Inherits @BeforeMethod and @AfterMethod from BaseTest
    
    @Test
    public void testParaBankHomepageLoads() {
        // BaseTest.setup() runs automatically BEFORE this
        // BaseTest.teardown() runs automatically AFTER this
    }
}
```

**Execution Count**: 60 times (30 tests × 2: before + after)

---

#### **src/main/java/driver/DriverFactory.java**

**Purpose**: Creates and configures WebDriver instances

**What it does**:
```java
public class DriverFactory {
    public static void initDriver(String browserName) {
        // Step 1: Download browser driver
        WebDriverManager.chromedriver().setup();
        // ↑ Automatically downloads ChromeDriver matching system Chrome
        
        // Step 2: Create Chrome browser instance
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");  // 1920x1080
        options.addArguments("--disable-notifications");
        
        WebDriver driver = new ChromeDriver(options);
        // ↑ Browser window opens here
        
        // Step 3: Store driver for test to use
        DriverManager.setDriver(driver);
    }
}
```

**Why WebDriverManager?**
- Without it: You must manually download ChromeDriver, keep it updated, specify its path
- With it: Automatically detects Chrome version, downloads matching driver, sets up path

```
Traditional Selenium:
├─ Download ChromeDriver 146 from SeleniumHQ
├─ Save to C:/drivers/chromedriver.exe
├─ Set system property: webdriver.chrome.driver = C:/drivers/...
└─ Hard to maintain when Chrome updates

With WebDriverManager:
├─ WebDriverManager.chromedriver().setup()
├─ Auto-detects Chrome version
├─ Auto-downloads matching driver
├─ Auto-sets properties
└─ One line of code!
```

**Execution Count**: 30 times (once per test)

---

#### **src/main/java/driver/DriverManager.java**

**Purpose**: Stores WebDriver safely for multithreading

**What it does**:
```java
public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    // ↑ Each thread gets its own driver instance
    
    public static WebDriver getDriver() {
        return driver.get();  // Get driver for THIS thread
    }

    public static void setDriver(WebDriver driverRef) {
        driver.set(driverRef);  // Store driver for THIS thread
    }

    public static void quit() {
        driver.get().quit();  // Close THIS thread's driver
        driver.remove();      // Clean up
    }
}
```

**Why ThreadLocal?**

```
Without ThreadLocal (Thread-unsafe):
├─ Private static WebDriver driver;
├─ Thread 1 sets: driver = Chrome instance 1
├─ Thread 2 sets: driver = Chrome instance 2
│  └─ OVERWRITES Thread 1's driver!
└─ Thread 1 gets Thread 2's driver (WRONG!)

With ThreadLocal (Thread-safe):
├─ Private static ThreadLocal<WebDriver> driver
├─ Thread 1 sets: driver.set(Chrome instance 1)
├─ Thread 2 sets: driver.set(Chrome instance 2)
│  └─ Stored separately per thread!
└─ Thread 1 gets: driver.get() → Chrome instance 1 (CORRECT!)
```

**Real example**:
```
Running 4 tests in parallel:
├─ Thread 1: DriverManager stores Chrome instance 1
├─ Thread 2: DriverManager stores Chrome instance 2
├─ Thread 3: DriverManager stores Chrome instance 3
└─ Thread 4: DriverManager stores Chrome instance 4

Each thread always gets its own browser!
```

**Execution Count**: 60 times (30 sets + 30 gets + 30 quits)

---

#### **src/main/java/utils/ConfigReader.java**

**Purpose**: Load configuration from properties file

**Configuration File**: `src/test/resources/config/config.properties`

```properties
baseURL=https://parabank.parasoft.com/
browser=chrome
timeout=20
username=testuser123
password=testpass123
```

**What it does**:
```java
public class ConfigReader {
    private static Properties prop = new Properties();

    static {
        // Runs once when class is loaded (STATIC INITIALIZATION)
        InputStream is = ConfigReader.class
            .getClassLoader()
            .getResourceAsStream("config/config.properties");
        
        prop.load(is);  // Load all properties into HashMap
    }

    public static String get(String key) {
        return prop.getProperty(key);  // Get value for key
    }
}
```

**Usage**:
```java
String baseURL = ConfigReader.get("baseURL");
// Returns: "https://parabank.parasoft.com/"

String browser = ConfigReader.get("browser");
// Returns: "chrome"

String timeout = ConfigReader.get("timeout");
// Returns: "20"
```

**Why externalize configuration?**

```
Without ConfigReader (Hard-coded):
├─ Development:
│  ├─ baseURL = "http://localhost:8080"
│  └─ browser = "chrome"
├─ Staging:
│  ├─ baseURL = "http://staging.example.com"
│  └─ browser = "firefox"
└─ Production:
   ├─ baseURL = "http://prod.example.com"
   └─ browser = "safari"
   
Problem: Change code for each environment! 😱

With ConfigReader (Externalized):
├─ Code stays same
├─ Only config file changes
├─ No code re-compilation needed
└─ Easy environment management! ✅
```

**Execution Count**: 1 time (static initialization when ConfigReader class loads)

---

#### **src/main/java/pages/BankingLoginPage.java**

**Purpose**: Page Object Model - encapsulates login page interactions

**What it does**:
```java
public class BankingLoginPage extends BasePage {
    // Locators (how to find elements)
    private By usernameField = By.name("customer.username");
    private By passwordField = By.name("customer.password");
    private By loginButton = By.xpath("//input[@value='Log In']");

    public BankingLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods (what actions to perform)
    public void enterUsername(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement element = wait.until(
            ExpectedConditions.elementToBeClickable(usernameField)
        );
        element.clear();
        element.sendKeys(username);
    }

    public void enterPassword(String password) {
        // Same pattern as enterUsername
    }

    public void clickLoginButton() {
        // Click the button
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
```

**Why Page Object Model?**

```
Without POM (Test code has element locators):
├─ Test method 1:
│  ├─ driver.findElement(By.name("customer.username")).sendKeys("user");
│  ├─ driver.findElement(By.name("customer.password")).sendKeys("pass");
│  └─ driver.findElement(By.xpath("//input[@value='Log In']")).click();
│
├─ Test method 2:
│  ├─ driver.findElement(By.name("customer.username")).sendKeys("user2");
│  ├─ driver.findElement(By.name("customer.password")).sendKeys("pass2");
│  └─ driver.findElement(By.xpath("//input[@value='Log In']")).click();
│
└─ Problem: Locators duplicated everywhere!
   If locator changes, update ALL tests! 😱

With POM (Page Object has element locators):
├─ LoginPage.java:
│  ├─ private By usernameField = By.name("customer.username");
│  └─ public void login(String user, String pass) { ... }
│
├─ Test method 1:
│  └─ loginPage.login("user", "pass");
│
├─ Test method 2:
│  └─ loginPage.login("user2", "pass2");
│
└─ Advantage: Locator in ONE place
   If locator changes, update ONCE! ✅
```

**Execution Count**: 2+ times (whenever tests need to login)

---

#### **src/test/java/tests/ParaBankUITest.java**

**Purpose**: Contains 12 REAL UI test methods

**Structure**:
```java
public class ParaBankUITest extends BaseTest {
    // Inherits @BeforeMethod and @AfterMethod from BaseTest
    
    @Test(priority = 1)
    public void testParaBankHomepageLoads() {
        driver = DriverManager.getDriver();
        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page title should exist");
        System.out.println("✓ Page Title: " + title);
    }

    @Test(priority = 2)
    public void testLoginPageAccessible() {
        driver = DriverManager.getDriver();
        loginPage = new BankingLoginPage(driver);
        // Try to find login form...
    }

    @Test(priority = 3)
    public void testRegisterNewAccount() {
        // Test registration flow...
    }
    
    // ... 9 more tests
}
```

**What each test does**:

| Test # | Name | Purpose |
|--------|------|---------|
| 1 | testParaBankHomepageLoads | Verify page loads with title & URL |
| 2 | testLoginPageAccessible | Check login form appears |
| 3 | testRegisterNewAccount | Test register link works |
| 4 | testLoginWithInvalidCredentials | Test error for wrong password |
| 5 | testLoginWithEmptyUsername | Test form validation |
| 6 | testLoginWithEmptyPassword | Test form validation |
| 7 | testPageElementsPresence | Find login button |
| 8 | testPasswordFieldType | Check password field type=password |
| 9 | testForgotPasswordLink | Verify forgot password link |
| 10 | testPageNavigationAndResponsiveness | Check page load time |
| 11 | testFormAction | Verify form method=post |
| 12 | testPageLoadWithoutErrors | Check page has content |

**Execution Count**: 12 times (one per @Test method)

---

### Configuration Files

#### **testng.xml**

**Purpose**: Tells TestNG and Maven which test classes to run

```
When Maven runs tests:
├─ Reads: testng.xml
├─ Finds: 3 test suites
├─ Finds: 30 @Test methods total
│  ├─ 12 in BankingFrameworkVerificationTest
│  ├─ 6 in BankingLoginTest
│  └─ 12 in ParaBankUITest
└─ Executes each test
```

#### **pom.xml**

**Purpose**: Maven configuration and dependency management

```
Contains:
├─ Project metadata
├─ All required dependencies
│  ├─ Selenium (for browser automation)
│  ├─ TestNG (for test execution)
│  ├─ Cucumber (for BDD)
│  ├─ WebDriverManager (for auto browser driver download)
│  └─ Allure (for reporting)
├─ Plugin configuration
│  └─ maven-surefire-plugin (runs tests using testng.xml)
└─ Compiler settings
   └─ Use Java 11
```

#### **config.properties**

**Purpose**: External configuration (no hardcoding!)

```
Contains:
├─ baseURL = https://parabank.parasoft.com/
├─ browser = chrome
├─ timeout = 20
├─ username = testuser123
├─ password = testpass123
└─ screenshot_path = test-output/screenshots/
```

---

## PART 6: HOW TESTS ARE CALLED & EXECUTED

### Complete Flow for One Test

Let's trace ONE test from start to finish:

```
Test: testParaBankHomepageLoads()
Location: src/test/java/tests/ParaBankUITest.java

STEP 1: TestNG discovers the test
├─ Reads testng.xml
├─ Finds: <class name="tests.ParaBankUITest"/>
├─ Loads class: ParaBankUITest
├─ Finds method: public void testParaBankHomepageLoads()
└─ Marked with: @Test

STEP 2: TestNG looks for @BeforeMethod
├─ Checks: ParaBankUITest class
├─ Not found in ParaBankUITest
├─ Checks: Parent class (BaseTest)
├─ Found: @BeforeMethod BaseTest.setup()
└─ Will execute this FIRST

STEP 3: TestNG executes @BeforeMethod
├─ Calls: BaseTest.setup()
│
│  STEP 3.1: DriverFactory.initDriver("chrome")
│  ├─ WebDriverManager.chromedriver().setup()
│  │  └─ Detects Chrome version (146)
│  │  └─ Downloads ChromeDriver 146 from internet
│  │     (or uses cached version)
│  │  └─ Saves to ~/.m2/repository/ or cache folder
│  │
│  ├─ new ChromeDriver(options)
│  │  └─ Launches Chrome browser window
│  │  └─ Sets window size to 1920x1080
│  │  └─ Disables notifications
│  │
│  └─ Sets up WebDriver instance
│
│  STEP 3.2: DriverManager.setDriver(driver)
│  ├─ Stores driver in ThreadLocal<WebDriver>
│  └─ Now tests can access driver via DriverManager.getDriver()
│
│  STEP 3.3: driver.get("https://parabank.parasoft.com/")
│  ├─ Navigates to ParaBank homepage
│  ├─ Waits for page to load
│  └─ Takes ~5-7 seconds
│
│  [Setup complete!]

STEP 4: TestNG executes @Test method
├─ Calls: testParaBankHomepageLoads()
│
│  public void testParaBankHomepageLoads() {
│  
│      // Line 1: Get the WebDriver instance
│      driver = DriverManager.getDriver();
│      // ↑ Gets the Chrome instance from ThreadLocal
│      
│      // Line 2: Assert driver exists
│      Assert.assertNotNull(driver, "WebDriver should be initialized");
│      // ✓ PASS (driver is not null)
│      
│      // Line 3: Get page title
│      String title = driver.getTitle();
│      // ↑ Selenium gets title from browser: "ParaBank Welcome Online Banking"
│      
│      // Line 4: Assert title exists
│      Assert.assertNotNull(title, "Page title should exist");
│      // ✓ PASS (title is "ParaBank Welcome Online Banking")
│      
│      // Line 5: Print title
│      System.out.println("✓ Page Title: " + title);
│      // Output: ✓ Page Title: ParaBank Welcome Online Banking
│      
│      // Line 6: Get current URL
│      String currentUrl = driver.getCurrentUrl();
│      // ↑ Selenium gets URL from browser: "https://parabank.parasoft.com/parabank/index.htm..."
│      
│      // Line 7: Assert URL contains "parabank"
│      Assert.assertTrue(currentUrl.contains("parabank"), "Should be on ParaBank website");
│      // ✓ PASS (URL contains "parabank")
│      
│      // Line 8: Print URL
│      System.out.println("✓ Current URL: " + currentUrl);
│      
│      // Line 9: Take screenshot
│      ScreenshotUtil.takeScreenshot(driver, "ParaBankHomepage");
│      // ↑ Saves screenshot to test-output/screenshots/ParaBankHomepage_20260326_182013.png
│  }
│
│  [Test execution complete!]

STEP 5: TestNG looks for @AfterMethod
├─ Checks: ParaBankUITest class
├─ Not found
├─ Checks: Parent class (BaseTest)
├─ Found: @AfterMethod BaseTest.teardown()
└─ Will execute this NOW

STEP 6: TestNG executes @AfterMethod
├─ Calls: BaseTest.teardown()
│
│  STEP 6.1: DriverManager.quit()
│  ├─ driver.quit()
│  │  ├─ Closes all browser windows
│  │  ├─ Ends WebDriver session
│  │  └─ Releases resources
│  │
│  └─ driver.remove()
│     └─ Removes driver from ThreadLocal
│        (cleanup for garbage collection)
│
│  [Teardown complete!]

STEP 7: TestNG records test result
├─ All assertions PASSED
├─ No exceptions thrown
├─ Test status: ✅ PASSED
└─ Add to report

STEP 8: TestNG repeats for next test
├─ Next test in ParaBankUITest: testLoginPageAccessible()
├─ Runs @BeforeMethod (launches new browser)
├─ Runs test logic
├─ Runs @AfterMethod (closes browser)
└─ Records result

[Repeat for all 30 tests...]

STEP 9: Maven generates reports
├─ Surefire report
│  └─ target/surefire-reports/
├─ Allure report
│  └─ allure-results/
└─ Print summary
   ✅ Tests run: 30
   ✅ Failures: 0
   ✅ BUILD SUCCESS
```

### Visual Timeline for One Test

```
Timeline (seconds)
0s ────────────────────────────────────────────────────────────────
   ├─ @BeforeMethod starts
   │  
0.5s ├─ WebDriverManager.chromedriver().setup()
   │  └─ (~1-2 seconds)
   │
1.5s ├─ new ChromeDriver()
   │  └─ Browser launches (~1 second)
   │
2.5s ├─ driver.get("https://parabank.parasoft.com/")
   │  └─ (~5 seconds to load page)
   │
7.5s ├─ @BeforeMethod complete
   │  ├─ @Test starts
   │
7.6s ├─ Test logic executes
   │  ├─ Get title
   │  ├─ Assert title
   │  ├─ Get URL
   │  ├─ Assert URL
   │  ├─ Take screenshot
   │  └─ (~0.2 seconds)
   │
7.8s ├─ @Test complete
   │  ├─ @AfterMethod starts
   │
7.9s ├─ driver.quit()
   │  └─ Browser closes (~0.1 seconds)
   │
8.0s ├─ @AfterMethod complete
   │  └─ Test PASSED ✅
   │
Total: ~8 seconds per test
30 tests × 8 seconds = 240 seconds = 4 minutes
(Plus compile time: 10 seconds)
(Plus report generation: 3 seconds)
= ~3 minutes 53 seconds
(Actual: 2:44 because some tests are faster)
```

---

## QUICK REFERENCE: Key Concepts

| Concept | What It Is | Purpose |
|---------|-----------|---------|
| **TestNG** | Test framework | Runs @Test methods, @Before/@After setup/teardown |
| **testng.xml** | XML config file | Defines which test classes to run |
| **@BeforeMethod** | Annotation | Runs BEFORE each test (setup: launch browser) |
| **@AfterMethod** | Annotation | Runs AFTER each test (teardown: close browser) |
| **@Test** | Annotation | Marks a method as a test case |
| **Cucumber** | BDD framework | Runs Gherkin feature files, maps to Java step definitions |
| **.feature** | Gherkin file | Human-readable test scenarios |
| **@Given/@When/@Then** | Annotations | Map feature file steps to Java methods |
| **Hooks** | Cucumber setup/teardown | @Before/@After for Cucumber scenarios |
| **Maven** | Build tool | Compiles code, downloads dependencies, runs tests |
| **pom.xml** | Maven config | Defines dependencies and plugins |
| **mvn clean test** | Maven command | Clean old build, compile, run tests |
| **DriverManager** | Thread storage | Stores WebDriver in ThreadLocal (thread-safe) |
| **DriverFactory** | Browser init | Creates WebDriver, launches browser |
| **ConfigReader** | Config loader | Loads config.properties file |
| **Page Object Model** | Design pattern | Encapsulates page interactions in separate classes |
| **WebDriverManager** | Auto driver download | Auto-downloads matching ChromeDriver for your Chrome |

---

## Summary for New Automation Testers

When you run `mvn clean test`:

1. **Maven cleans** old builds
2. **Maven compiles** your Java code
3. **Maven downloads** dependencies (Selenium, TestNG, etc.)
4. **Maven-Surefire reads** testng.xml
5. **TestNG discovers** 30 @Test methods across 3 classes
6. **For each test**:
   - Runs @BeforeMethod (launches browser)
   - Runs test logic (assertions, interactions)
   - Runs @AfterMethod (closes browser)
7. **Tests interact with** ParaBank real website
8. **Results recorded** in test reports
9. **All 30 tests PASS** ✅

---

This is the technical explanation you need to understand the complete automation testing framework!

