Feature: UG4- Recommended Intake
  Scenario Outline: Given I have linked an activity from my Garmin watch, When I click a dedicated UI element to see recommended food, Then I am presented with a set meals based on my calorie intake preference.
    Given I logged in
    And There are meals
    And I have set my calorie intake preference to <calorieIntakePreference>
    When I click a dedicated UI element to see recommended food based on my <caloriesBurnt>
    Then I am presented with a set meals based on my <calorieIntakePreference> <caloriesBurnt>
    Examples:
      | calorieIntakePreference | caloriesBurnt|
      | "bulk"                  |   500        |
      | "cut"                   | 1000         |
      | "maintain"              |  400         |

  Scenario: User adds a singular recommended meal
    Given I logged in
    When I add one recommended meal
    Then I am able to see the one meal in the database

  Scenario: User adds multiple recommended meals
    Given I logged in
    When I add multiple recommended meals
      Then I am able to see multiple meals in the database

