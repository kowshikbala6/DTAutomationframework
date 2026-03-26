Feature: Banking Login Functionality

  Scenario: Successful login with valid credentials
    Given user is on the banking login page
    When user logs in with username "admin@banking.com" and password "admin"
    Then user should be logged in successfully

  Scenario: Failed login with invalid password
    Given user is on the banking login page
    When user enters username "admin@banking.com"
    And user enters password "wrongPassword"
    And user clicks the login button
    Then login should fail with an error message

  Scenario: Empty username field
    Given user is on the banking login page
    When user enters username ""
    And user enters password "admin"
    And user clicks the login button
    Then login should fail with an error message

  Scenario: Empty password field
    Given user is on the banking login page
    When user enters username "admin@banking.com"
    And user enters password ""
    And user clicks the login button
    Then login should fail with an error message

  Scenario: Forgot password functionality
    Given user is on the banking login page
    When user clicks forgot password link
    Then login should fail with an error message
