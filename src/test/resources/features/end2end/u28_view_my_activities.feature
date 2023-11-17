Feature: U28 - View My Activities

  Scenario: AC1 -  Given I logged in and anywhere on the system, when I click on a UI element to see all
  my activities, then I see a list of all activities from all the teams I belong to and all the personal
  activities I created for myself.
    Given I am logged in
    When I click View Activities UI element
    Then I see all my activities

