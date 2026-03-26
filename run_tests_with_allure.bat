@echo off
REM Script to run tests locally with Allure reporting
REM Usage: run_tests_with_allure.bat

echo ========================================
echo Banking Automation Framework - Test Runner
echo ========================================
echo.

REM Check Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    exit /b 1
)

REM Check Maven
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    exit /b 1
)

echo Running tests...
echo.

REM Run tests
mvn clean test -Dmaven.test.failure.ignore=true

if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Some tests failed
)

echo.
echo Generating Allure report...

REM Generate Allure report
mvn allure:report

if %ERRORLEVEL% EQU 0 (
    echo.
    echo SUCCESS: Tests completed. Allure report generated.
    echo Report location: target/site/allure-report/index.html
    echo.
    pause
) else (
    echo.
    echo ERROR: Failed to generate Allure report
    echo.
    pause
    exit /b 1
)
