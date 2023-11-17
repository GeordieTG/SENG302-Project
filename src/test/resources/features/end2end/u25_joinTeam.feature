Feature: U25 - Join Team

  Scenario: AC1 - View My Teams button shows join team form
    Given I am logged in as team 1
    And I am anywhere on the system
    When I hit the view teams button
    Then I can choose to join a new team

  Scenario: AC2 - When I input an invitation token associated with a team, I am added to that team
    Given I am logged in as team 1
    And I am being shown an input to join a team
    When I input an invitation token that is associated to a team in the system
    Then I am added as a member to this team

  Scenario: AC3 - When I input an invitation token that is not associated with a team, i get an error message
    Given I am logged in as team 1
    And I am being shown an input to join a team
    When I input an a valid invitation token that is not associated to a team in the system
    Then An error message tells me the token is invalid

  Scenario: AC4 - When I join a new team, I see that team when I view my teams
    Given I am logged in as team 0
    And I have joined a new team
    When I hit the view teams button
    Then I see the new team I just joined
