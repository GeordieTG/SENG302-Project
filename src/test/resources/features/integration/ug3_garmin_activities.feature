Feature: UG2- Garmin Activities

  Scenario: AC3- Given I select Garmin activities, When I confirm this choice, Then it is persisted in the database
    Given I logged in
    And I have an activity for garmin activity
    When I save a selected Garmin Activity
    Then the garmin activity is persisted in the database