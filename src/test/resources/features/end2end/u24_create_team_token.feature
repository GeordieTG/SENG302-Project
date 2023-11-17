Feature: U24 - Create team invitation token.

  Scenario: AC1 - When I am on the team profile page of a team I manage, then I can see a unique secret token
  for my team that is exactly 12 char long with a combination of letters and numbers, but no special characters.
    Given I have logged in
    And I have created a team "myTeam"
    When I am on the team profile page of the created team "myTeam"
    And A unique token was created for the team
    Then I can see a unique secret token
    And Token is exactly 12 characters long
    And Token is a combination of only letters and numbers

  Scenario: AC2 - Given I am on the team profile page, when I generate a new secret token for my team,
  then a new token is generated, and this token is unique across the system, and that token is not a repeat of a previous token.
    Given I am logged in
    And I have created a team "myTeam"
    When I am on the team profile page of the created team "myTeam"
    And A unique token was created for the team
    Then A new token is generated
