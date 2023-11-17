Feature: UG1- Connect to Garmin Watch

  Scenario: AC2 - Given I am logged in, when I am setting my daily activity goals, then I can
  specify a goal for my steps, calories burnt, distance travelled, and total activity time.
    Given I logged in
    When I submit a form with my daily activity goals
    Then my goals are saved in the database

  Scenario: AC2 - Given am setting my activity goals, When I enter float values for the allowed
  fields, Then I my goals are saved to the database
    Given I logged in
    When I submit a form with my daily activity goals containing floats
    Then my goals are saved in the database as floats

  Scenario: AC2 - Given am setting my activity goals, When I enter float values for the allowed
  fields, Then I my goals are saved to the database
    Given I logged in
    When I submit a form with my daily activity goals containing a subset of possible goals
    Then my goals are saved in the database as a subset of goals