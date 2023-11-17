Feature: U13 - Search Teams

  Scenario: AC2 - Given I am on the search form and enter a search string of at least 3
  characters, when I hit the search button, then I see a list of results composed of all teams
  matching the search query showing the teams’ profile picture, name, location, and sport.
    Given I logged in
    And there is a team with name "Raith's team" and "Football" and "Hokkaido"
    When I enter a search string of at least 3 characters
    Then I see a list of all the teams matching the search query