Feature: U33 - Edit line-up for game


  Scenario: AC1 - When I am on the activity page of a team-based activity with either the game or
  friendly type, then I can add a line-up from the list of existing formations for that team.
    Given I logged in
    And I have activity data
    When I am viewing a game activity
    Then I can add a line-up from the list of existing formations for that team.

  Scenario: AC2 - Given I am on the activity page of a team-based activity with either
  the game or friendly type, when I select a formation from the existing team’s
  formation for that game, then the formation is displayed in the activity page.
    Given I logged in
    And I have activity data
    When I select a formation
    Then the formation is displayed in the activity page.

  Scenario: AC3 - Given I have selected a formation for the game, when I click on a player’s icon, then I can select a
  player from my team to fill in that position.
    Given I logged in
    And I have activity data
    When I am viewing a game activity
    Then I am given the option to see the members of my team to put in the lineup


  Scenario: Saving formation to the database
    Given I logged in
    When I set up a lineup for the activity and click the save button
    Then I can see the lineup saved in the database

  Scenario: Total games increases when you add a lineup after stats have been added
    Given I logged in
    And I have an activity with statistics and no lineup
    When I set up a lineup for my activity
    Then my total games should increase