@echo off
REM Banking Automation Framework - Test Execution Script (Windows)

echo ==================================================
echo Banking Automation Framework - Test Runner
echo ==================================================
echo.

REM Check if Maven is available
mvn --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    exit /b 1
)

set TEST_OPTION=%1
if "%TEST_OPTION%"=="" set TEST_OPTION=all

echo Checking Maven installation...
mvn --version
echo.

REM Clean previous builds
echo Cleaning previous builds...
mvn clean

if "%TEST_OPTION%"=="login" (
    echo Running Banking Login Tests...
    mvn test -Dtest=tests.BankingLoginTest
) else if "%TEST_OPTION%"=="dashboard" (
    echo Running Banking Dashboard Tests...
    mvn test -Dtest=tests.BankingDashboardTest
) else if "%TEST_OPTION%"=="account" (
    echo Running Banking Account Tests...
    mvn test -Dtest=tests.BankingAccountTest
) else if "%TEST_OPTION%"=="transfer" (
    echo Running Banking Transfer Tests...
    mvn test -Dtest=tests.BankingTransferTest
) else if "%TEST_OPTION%"=="api" (
    echo Running Banking API Tests...
    mvn test -Dtest=api.BankingApiTest
) else if "%TEST_OPTION%"=="cucumber" (
    echo Running Cucumber Feature Tests...
    mvn test -Dtest=runners.BankingTestRunner
) else if "%TEST_OPTION%"=="all" (
    echo Running All Tests...
    mvn test
) else if "%TEST_OPTION%"=="report" (
    echo Generating Allure Report...
    mvn allure:report
    mvn allure:serve
) else (
    echo Usage: %0 [login^|dashboard^|account^|transfer^|api^|cucumber^|all^|report]
    echo.
    echo Examples:
    echo   %0 login      - Run login tests
    echo   %0 dashboard  - Run dashboard tests
    echo   %0 account    - Run account tests
    echo   %0 transfer   - Run transfer tests
    echo   %0 api        - Run API tests
    echo   %0 cucumber   - Run Cucumber tests
    echo   %0 all        - Run all tests
    echo   %0 report     - Generate Allure report
    exit /b 1
)

echo.
echo Test execution completed!
