Feature: U25 - Join a team

  Scenario: A user cannot join a team if they are already a member
    Given I'm logged in
    And I have a team called "Bishops"
    And I generate an invite token for my team
    When I go to join a team with my own teams token
    Then My role in my team does not change