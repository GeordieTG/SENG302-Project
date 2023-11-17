Feature: U33 - Edit Lineup for game

  Scenario: AC1 - When I am on the activity page of a team-based activity with either the game or friendly type,
  then I can add a line-up from the list of existing formations for that team.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    When I see the activity details with the team
    Then There should be a section for the user to add a lineup for the game

  Scenario: AC2 - Given I am on the activity page of a team-based activity with either the game or friendly type,
  when I select a formation from the existing team’s formation for that game, then the formation is displayed in the
  activity page.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    When I select a formation from the formation dropdown and set up
    Then Then the formation is displayed on the screen

  Scenario: AC3 - Given I have selected a formation for the game, when I click on a player’s icon, then I can select a
  player from my team to fill in that position.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a formation from the formation dropdown and set up
    When I click on a players icon
    Then I can select a player from my team to fill in that position

    ## AC4 - Duplicates to be tested manually

  # Name is a dropdown so will automatically display when selected
  Scenario: AC4 - When I select a player for a position, their profile picture is shown on the line-up and their first
  name is shown below their picture.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    When I select a player as a starter
    Then The starter is displayed on the screen

  Scenario: AC5 - When I have selected all players to fill in the formation for the game, then I can select one or more
  players who are substitutes for that game.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a simple formation from the formation dropdown and set up
    When I can select a player from my team to fill in that position
    Then I can see a section for substitutes


  #Name is a dropdown so will automatically display when selected
  Scenario: AC6 - When I have selected substitutes for that game, then their full name and pictures are displayed next
  or below the line-up.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form with a simple formation
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a simple formation from the formation dropdown and set up
    And I can select a player from my team to fill in that position
    When I select a player as a substitute
    Then The sub is displayed on the screen

    ## AC7 and AC8 to be added once saving functionality is complete

  Scenario: AC7 - Given I have selected a formation for the game, when I have not filled in all
  positions with a player (excluding substitutes that can be empty), then I cannot save the
  activity and I am shown an error message telling me the line-up is not complete.
  player from my team to fill in that position.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a formation from the formation dropdown and set up
    When I don't fill in all the positions with players
    Then An error message tells me that the formation is not complete

  Scenario: AC8 - Given I am on the activity page of a team-based activity with either the game
  or friendly type, when I cancel, then the current changes to the formation are ignored
  (e.g., if I created a line up, none is created, if I updated the line-up, changes are not
  persisted and the line-up is restored to its original state).
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I see the activity details with the team
    And I select a formation from the formation dropdown and set up
    When I cancel
#    Then The current changes to the formation are ignored