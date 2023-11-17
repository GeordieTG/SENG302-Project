Feature: U38.2 - View Club

  Scenario: AC4.1 - Given I am on the 'Team Search' page, when I search by name Then I can see both Team and Clubs as the results
    Given I logged in
    And there are multiple teams
    And there are multiple clubs
    And there is a team with name "Liam's Team" and "Christchurch" and "New Zealand"
    And there is a club with name "Liam's Club" and "Christchurch" and "New Zealand"
    And I am on the team search page
    When I search "Liam"
    Then I see 2 results

  Scenario: AC4.2 - Given I am on the 'Team Search' page, when I search by city Then I can see both Team and Clubs as the results
    Given I logged in
    And there are multiple teams
    And there are multiple clubs
    And there is a team with name "Liam's Team" and "Christchurch" and "New Zealand"
    And there is a club with name "Liam's Club" and "Christchurch" and "New Zealand"
    And I am on the team search page
    When I search "Christchurch"
    Then I see 2 results

  Scenario: AC4.3 - Given I am on the 'Team Search' page, when I search by country Then I can see both Team and Clubs as the results
    Given I logged in
    And there are multiple teams
    And there are multiple clubs
    And there is a team with name "Liam's Team" and "Christchurch" and "New Zealand"
    And there is a club with name "Liam's Club" and "Christchurch" and "New Zealand"
    And I am on the team search page
    When I search "New Zealand"
    Then I see 2 results

  Scenario: AC7
    Given I logged in
    And I have created a new club or viewing an existing club,
    When I am on the club's profile page,
    Then I can see a list of team that are part of the club.

  Scenario: AC6
    Given I logged in
    And I have created a new club or viewing an existing club,
    When I am on the club's profile page,
    Then I can see the club's name, club logo or profile picture, location, and sports

  Scenario: I see the club owner on the club's profile page
    Given I logged in
    And I have created a new club or viewing an existing club,
    When I am on the club's profile page,
    Then I see the owner of the club
