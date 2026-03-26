#!/bin/bash

# Banking Automation Framework - Test Execution Script

echo "=================================================="
echo "Banking Automation Framework - Test Runner"
echo "=================================================="
echo ""

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    exit 1
fi

# Get the option from command line
TEST_OPTION=${1:-all}

echo "Maven Version:"
mvn --version
echo ""

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

case "$TEST_OPTION" in
    login)
        echo "Running Banking Login Tests..."
        mvn test -Dtest=tests.BankingLoginTest
        ;;
    dashboard)
        echo "Running Banking Dashboard Tests..."
        mvn test -Dtest=tests.BankingDashboardTest
        ;;
    account)
        echo "Running Banking Account Tests..."
        mvn test -Dtest=tests.BankingAccountTest
        ;;
    transfer)
        echo "Running Banking Transfer Tests..."
        mvn test -Dtest=tests.BankingTransferTest
        ;;
    api)
        echo "Running Banking API Tests..."
        mvn test -Dtest=api.BankingApiTest
        ;;
    cucumber)
        echo "Running Cucumber Feature Tests..."
        mvn test -Dtest=runners.BankingTestRunner
        ;;
    all)
        echo "Running All Tests..."
        mvn test
        ;;
    report)
        echo "Generating Allure Report..."
        mvn allure:report
        mvn allure:serve
        ;;
    *)
        echo "Usage: $0 {login|dashboard|account|transfer|api|cucumber|all|report}"
        echo ""
        echo "Examples:"
        echo "  $0 login      - Run login tests"
        echo "  $0 dashboard  - Run dashboard tests"
        echo "  $0 account    - Run account tests"
        echo "  $0 transfer   - Run transfer tests"
        echo "  $0 api        - Run API tests"
        echo "  $0 cucumber   - Run Cucumber tests"
        echo "  $0 all        - Run all tests"
        echo "  $0 report     - Generate Allure report"
        exit 1
        ;;
esac

echo ""
echo "Test execution completed!"
