Feature: U38.3 - Update Club

  Scenario: AC1 - When I click a dedicated UI element to update a club,
  then I see an edit club form with the fields pre-populated
    Given I logged in
    And I have a team with a club
    When I want to edit the club
    Then I can see the field prepopulated

  Scenario: Given I am editing a club, when I add to the clubs teams, Then the clubs teams are
  updated
    Given I logged in
    And I have a club
    And I have a team for a club
    When I add to the clubs teams
    Then the clubs teams are updated

  Scenario: Given I am editing a club, when I remove from the clubs teams, Then the clubs teams are
  updated
    Given I logged in
    And I have a club
    And I have a team
    When I remove from the clubs teams
    Then the clubs teams are updated to have no teams

  Scenario: T337 - G: I am on the edit club page,
  W: There are teams in the club already,
  T: I am unable to change the sport of the club
    Given I logged in
    And I have a team with a club
    When I want to edit the club
    Then I am unable to change the sport of the club

  Scenario: T350 - G: I am viewing a club that I manage
  W: I click a dedicated UI element to update a club,
  T: I see an edit club form with the fields pre-populated
    Given I logged in
    When There is a team with a club
    Then I cannot edit the club
