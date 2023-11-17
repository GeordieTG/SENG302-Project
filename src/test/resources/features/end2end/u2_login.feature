Feature: U2 - Login

  Scenario: AC1 - Login Button shows Login Form
    Given I connect to the systems main URL
    When I hit the login button
    Then I see a login form