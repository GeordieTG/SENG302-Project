Feature: U33 - View aggregated team statistics

  Scenario: AC6 - When I click on a most recent activity on the teams page, I can see the details
  of that activity
    Given I am logged in
    And I am on my team’s profile which id is 2
    When I click on a recent activity
    Then I can see the details of the activity
