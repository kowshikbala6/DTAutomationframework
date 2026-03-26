Feature: Banking Transfer Functionality

  Background:
    Given user is on the banking login page
    When user logs in with username "admin@banking.com" and password "admin"
    Then user should be logged in successfully
    And user is on the banking dashboard
    When user clicks on transfer money
    Then transfer page should be displayed

  Scenario: Successful fund transfer
    When user selects from account "1234567890"
    And user enters beneficiary account "9876543210"
    And user enters transfer amount "1000"
    And user enters description "Test Transfer"
    And user submits the transfer
    Then transfer should be successful
    And success message should be displayed

  Scenario: Transfer with missing beneficiary account
    When user selects from account "1234567890"
    And user enters transfer amount "1000"
    And user submits the transfer
    Then transfer should fail with error message

  Scenario: Transfer with invalid amount
    When user selects from account "1234567890"
    And user enters beneficiary account "9876543210"
    And user enters transfer amount "999999999"
    And user submits the transfer
    Then transfer should fail with error message

  Scenario: Cancel transfer
    When user clicks cancel on transfer
    Then transfer page should be closed
