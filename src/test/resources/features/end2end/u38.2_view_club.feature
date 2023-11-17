Feature: U38.1 - Create Club

  Scenario:  AC8
    Given I am logged in
    And I have created a team "myTeam"
    And I created a new club or viewing an existing club,
    When I am on the club's profile page looking at the list of teams within the club,
    And I can click on any of the teams on the list
    Then be redirected to the team's profile page.

#  Scenario: AC3 - Display working link to the club from the team profile
#    Given I am logged in
#    And I have created a team "testTeam"
#    And I created a club "testClub" with team "testTeam"
#    When I am on the team profile page of the created team "testTeam"
#    And I click on the club logo
#    Then I am on "testClub" profile page
#
#  Scenario: AC2 - Team profile page displays club name and logo if in a club
#    Given I am logged in
#    And I have created a team "testTeam"
#    And I created a club "testClub" with team "testTeam"
#    When I am on the team profile page of the created team "testTeam"
#    Then I can see "testClub" name and logo


  Scenario: AC2 - Team profile page displays club name and logo if in a club
    Given I am logged in
    And I have created a team "testTeam"
    When I am on the team profile page of the created team "testTeam"
    Then I can see "No Club" name and default image
