#Feature: U26 - Edit team member roles.
#  Scenario: AC1 - See a list of team member roles
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "myTeam"
#    When I click on edit member roles
#    Then I see the list of team members and their role
#
#  Scenario: AC2 - The roles can only be manager, coach, or member
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "myTeam"
#    When I click on edit member roles
#    Then There are only three roles: manager, coach, and member
#
#  Scenario: AC3 - User must confirm before changing team roles
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "myTeam"
#    When I click on edit member roles
#    And I edit a member role
#    Then I must confirm the change for the change to take place
#
#  Scenario: AC4 - Multiple member roles can be changed at once
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "MyTeam"
#    When I click on edit member roles
#    And I edit more than one member roles
#    Then I can change more than one role at once
#    And I can save the changes at once
#
#  Scenario: AC5 - Manager user can edit team profile and generate new invitation tokens
#    Given I am logged in
#    And I create a team "myTeam"
#    When I navigate to "myTeam"
#    Then I can edit team profile
#    And I can generate new invitation tokens
#
#  Scenario: AC6 - When editing roles, at least one Manager must exit within a team
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "myTeam"
#    When I edit team roles and change to have no managers
#    Then I receive an error message
#    And The error message says I must have at least one manager to save the change
#
#  Scenario: AC7 -  When editing roles, at most three Manager can exist within a team
#    Given I am logged in
#    And I create a team "myTeam"
#    And I navigate to "myTeam"
#    When I edit team roles and change to have four managers
#    Then I receive an error message
#    And The error message says I can only have maximum three managers in a team at once
#
#
