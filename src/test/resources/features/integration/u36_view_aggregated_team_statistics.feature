Feature: U34 - Add activity statistics

  ## TODO add total games

  Scenario: AC1 -  The teams win-loss-draw-gamesPlayed should be all 0 for a newly made team and displayed on the team
  page
    Given I logged in
    And I have a team
    When I am viewing my teams page
    Then I can see their win, loss, draws and total games played all as 0

  Scenario Outline: AC1 -  The teams wins and games played should increase when a statistics form is entered with a win for the
  team and displayed on the team page
    Given I logged in
    And I have a team
    And a <outcome> is recorded for my team
    When I am viewing my teams page
    Then my teams <outcome> should increase by one
    Examples:
      | outcome |
      | "win"   |
      | "draw"  |
      | "loss"  |

  Scenario: AC2 - If my team plays five games, my last five trend should reflect the results
    Given I logged in
    And I have a team
    When a "win" is recorded for my team
    And a "loss" is recorded for my team
    And a "draw" is recorded for my team
    And a "win" is recorded for my team
    And a "loss" is recorded for my team
    Then my teams last five trend should reflect the results

    ## Attempted to create another test with more than 6 games played however I believe the sorting of timestamp becomes
    ## random as the events are being created within the same timestamp in a test environment

  Scenario: AC3 - When viewing my team I want to be able to see the top five goal scorers of my team
    Given I logged in
    And I have a team with many members
    When a game is played with various goal scorers
    Then I should be able to see the top five goal scorers of my team

  Scenario: AC4 - When viewing my team I want to be able to see the top five players in my team with the greatest playtime one sub
    Given I logged in
    And I have a team with many members
    When a game is played with one substitution
    Then I should be able to see the top five players with the greatest playtime with one substitute

#  Scenario: AC4 - When viewing my team I want to be able to see the top five players in my team with the greatest playtime multiple subs
#    Given I logged in
#    And I have a team with many members
#    When a game is played with multiple substitution
#    Then I should be able to see the top five players with the greatest playtime with multiple substitute

  Scenario: AC4 - When viewing my team I want to be able to see the top five players in my team with the greatest playtime with no subs
    Given I logged in
    And I have a team with many members
    When a game is played with no substitution
    Then I should be able to see the top five players with the greatest playtime with no substitution

  Scenario: AC5 - If my team plays five games, I should be able to retrieve the details of my teams five most recent
  games
    Given I logged in
    And I have a team
    When my team plays 5 games
    Then I should be able to retrieve my teams five most recent games

  ## AC6 to be done in End to End Testing

  Scenario: AC7 - Given my team has played any number of games, I should be able to retrieve my teams game history
    Given I logged in
    And I have a team
    When my team plays 10 games
    Then I should be able to retrieve my teams game history


  Scenario: - Given my team participated in the activity of training or other( not game or friendly) , I should see
  other activity when i navigate to see detail of activity in the team page.

    Given I logged in
    And I have a team
    When the team has played in the training and competition
    Then i should be able to retrieve the team activity lists include the training and competition
