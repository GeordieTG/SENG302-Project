Feature: UC1 - Tracking Nutrition

  Scenario: AC6 - Given I am on the View My Nutrition Page, When I click a dedicated UI element for adding a meal,Then I can add a meal to my food diary
    Given I logged in
    When I search for the food "big mac"
    And I select the first result
    And I select the first serving size
    And I select 1 as the quantity
    And I submit the add food form
    Then The food is persisted in the database




