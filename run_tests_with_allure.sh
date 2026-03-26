#!/bin/bash

# Script to run tests locally with Allure reporting
# Usage: bash run_tests_with_allure.sh

echo "========================================"
echo "Banking Automation Framework - Test Runner"
echo "========================================"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    exit 1
fi

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    exit 1
fi

echo "Running tests..."
echo ""

# Run tests
mvn clean test -Dmaven.test.failure.ignore=true

if [ $? -ne 0 ]; then
    echo "WARNING: Some tests failed"
fi

echo ""
echo "Generating Allure report..."

# Generate Allure report
mvn allure:report

if [ $? -eq 0 ]; then
    echo ""
    echo "SUCCESS: Tests completed. Allure report generated."
    echo "Report location: target/site/allure-report/index.html"
    echo ""
else
    echo ""
    echo "ERROR: Failed to generate Allure report"
    echo ""
    exit 1
fi
