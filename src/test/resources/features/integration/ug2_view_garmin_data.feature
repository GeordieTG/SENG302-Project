Feature: UG2- View Garmin Data

  Scenario: AC1 - Given I logged in, When I am not connected to my Garmin Watch, Then the garmin
    statistics display is overlayed with a blur effect
    Given I logged in
    When I am on my profile page without being connected to my garmin watch
    Then the garmin statistics display is overlayed with a blur effect