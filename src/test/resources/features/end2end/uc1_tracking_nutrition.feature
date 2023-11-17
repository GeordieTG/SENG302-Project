Feature: UC1 - Tracking Nutrition

  Scenario: AC6 - Given I am on the View My Nutrition Page, When I click a dedicated UI element for adding a meal,Then I can add a meal to my food diary
    Given I am logged in
    And I am on my nutrition page
    When I click the add food button
    And I enter a search query
    And I select a food from the search results
    And I click the button for adding the food
    Then I see the food reflected in my daily nutritional overview

  Scenario: AC6 - Given I am on the View My Nutrition Page, When I click a dedicated UI element for adding a meal,Then I can add a meal to my food diary
    Given I am logged in
    And I am on my nutrition page
    Then I see the add food button

  Scenario: AC5 - Given I am on my Nutrition Statistics Page, When I am on my Nutrition  Statistics Page, Then I am able to see my nutritional statistics for the day
    Given I am logged in as team 2
    And I am on my profile page
    Then I see my daily nutritional overview



