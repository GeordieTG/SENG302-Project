Feature: U34 - Add Activity Statistic

  Scenario: AC2 Given - there is a UI element to add Activity statistics, When I Click the element
  then a page or element for inputting adding statistics is shown.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    When I click on a UI element to add statistics
    Then I see a page to input statistics

  Scenario: AC7 - A user should be able to enter the name of a person who scored along with the time in the activity.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    When I click on a UI element to add statistics
    Then I see inputs to record a scoring event

  Scenario: AC8 - A user should be able to enter the name of a person who was subbed out of the game and subbed in of
  the game along with the time in the activity.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    When I click on a UI element to add statistics
    Then I see inputs to record a substitution event

  Scenario: AC10 - When the user clicks the add activity facts button they are given the option to add a fact description
  and a time stamp.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    When I click on a UI element to add facts
    Then I see a page to input facts with appropriate fields

  Scenario: AC11 - Given I am on the view activity page, Then I can determine the outcome of the activity
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    When I click on a UI element to add statistics
    Then I can see inputs to record the outcome of the activity

  Scenario Outline: AC4-a: Given I want to add a score, When I enter scores in the format {number} or {number-number}
  for a team, Then the other team must be in the same format.

    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    And I save the line-up
    And I click on a UI element to add statistics
    When I enter scores in the format <teamScore> for a team, with the other team must be in the same format <oppoScore>.
    Then I can see the input are valid
    Examples:
      | teamScore | oppoScore |
      | "3"       | "4"       |
      | "0"       | "0"       |
      | "3"       | "3"       |
      | "4-4"     | "3-3"     |
      | "0-0"     | "1-3"     |


  Scenario Outline: AC4-b: Given I want to add a score, When I enter scores in the format {number} or {number-number}
  for a team, Then the other team must be in the same format.

    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I click on a UI element to see all activities
    And I click on a UI element to see my first teams activities
    And I click on a UI element to add statistics
    When I enter scores in the format <teamScore> for a team, with the other team must be in the same format <oppoScore>.
    Then I can see the input are invalid
    Examples:
      | teamScore | oppoScore |
      | "3"       | "a"       |
      | "b"       | "4"       |
      | "-1"      | "0"       |
      | "0"       | "-5"      |
      | "a"       | "b"       |
      | "a"       | "-1"      |
      | "-1"      | "-1"      |
      | "a-a"     | "-1"      |
      | "3-4"     | "6"       |
      | "4"       | "0-1"     |
