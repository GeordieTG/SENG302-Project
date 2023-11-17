Feature: U37 - View aggregated personal statistics

  Scenario: AC4 - When I am on the view activity page, then I can see my personal scoring events
    Given I logged in
    And I have a game activity for one of my teams
    And I have scored points in that game
    When I am viewing that activities page
    Then I can see how many goals I scored with the time in that activity

  Scenario: AC4 - When I am on the view activity page and I have no scoring
  events then I can no scoring events and a message telling me there are no points
    Given I logged in
    And I have a game activity for one of my teams
    When I am viewing that activities page
    Then I can see no points

  Scenario: AC4 - When I am on the view activity page, then I can see the total time I played
    Given I logged in
    And I have a game activity for one of my teams
    And I have substitution events in that game
    When I am viewing that activities page
    Then I can see the total time I played

  Scenario: Given I am on the profile page, when I am a part of a team with no games played, then I can see them on screen.
    Given I logged in
    And I have a game activity for one of my teams
    When I view my profile page
    Then I can see that I do not have statistics to show

  Scenario: Given I am on the profile page, when I don't have personal statistics to show, then I can't see them on
  screen.
    Given I logged in
    When I view my profile page
    Then I can see that I do not have statistics to show

