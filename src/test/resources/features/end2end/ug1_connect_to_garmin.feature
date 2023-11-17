Feature: UG1 - Connect to Garmin

  Scenario: AC1 - Given I logged in, When I am on my User Profile page , Then I can see a dedicated
  UI element, for connecting to the Garmin watch.
    Given I have logged in as user 10
    When I am on my profile page
    Then I can see a button for connecting to the Garmin watch

  Scenario: AC2 - Given I am on my profile page, When I click a dedicated UI element, Then I am
  taken to the Garmin portal to login.
    Given I have logged in as user 10
    When I am on my profile page
    And I click the button for connecting to the Garmin watch
    Then I am redirected to the Garmin Portal to log in