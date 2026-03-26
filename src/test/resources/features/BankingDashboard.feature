Feature: Banking Dashboard Functionality

  Background:
    Given user is on the banking login page
    When user logs in with username "admin@banking.com" and password "admin"
    Then user should be logged in successfully

  Scenario: View account details
    Given user is on the banking dashboard
    When user clicks on account details
    Then account details page should be displayed
    And account number should be visible
    And account balance should be displayed

  Scenario: View transfer page
    Given user is on the banking dashboard
    When user clicks on transfer money
    Then transfer page should be displayed

  Scenario: View deposits
    Given user is on the banking dashboard
    When user clicks on deposits
    Then user should be logged in successfully

  Scenario: View transaction history
    Given user is on the banking dashboard
    When user clicks on transaction history
    Then user should be logged in successfully

  Scenario: Logout from dashboard
    Given user is on the banking dashboard
    When user clicks logout
    Then user should be logged out successfully
