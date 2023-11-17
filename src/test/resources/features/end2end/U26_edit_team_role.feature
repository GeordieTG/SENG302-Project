#Feature: U26 Edit team role
#
#  Scenario: AC1 - Team member tab show the team member table
#    Given I am logged in
#    And  I am on my team’s profile
#    When when I click on a UI element to edit the members role,
#    Then I see the list of members and their roles in the team.
#
#
#  Scenario: AC2 - Team member role should be one of the manger, coach,or member
#    Given I am logged in
#    And  I am on my team’s profile
#    When when I click on a UI element to edit the members role,
#    Then their roles can be one of “manager”, “coach”, or “member”.
#
#  Scenario Outline: AC3 - Update Roles, When I change people to acceptable roles, changes should be saved
#    Given I have logged in as user 4
#    And I am on my team’s profile that I manage
#    And I click on a UI element to edit the members role
#    And I change <manager> people to manager and I change <coach> people to coach
#    When I hit the save changes button
#    Then I am on my teams profile
#    And I see a success message about "rolesHaveChangedToast"
#    Examples:
#    | manager | coach |
#    | 1       | 2     |
#    | 2       | 0     |
#    | 2       | 8     |
#    | 1       | 5     |
#
#
#  Scenario: AC5 - Update Roles, When no managers in a team, then error is shown and changes should not be save
#    Given I have logged in as user 4
#    And I am on my team’s profile that I manage
#    And I click on a UI element to edit the members role
#    And I change my role from manager to coach
#    When I hit the save changes button
#    Then I see an error message about "NoEnoughManager"
#    And My changes are not saved
#
#  Scenario: AC6 - Update Roles, When too many managers in a team, then error is shown and changes should not be saved
#    Given I have logged in as user 4
#    And I am on my team’s profile that I manage
#    And I click on a UI element to edit the members role
#    And I change 4 people to manager
#    When I hit the save changes button
#    Then I see an error message about "tooManyManagerToast"
#    And My changes are not saved