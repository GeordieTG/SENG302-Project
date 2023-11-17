Feature: U29 - View Team Activity

  Scenario: AC1 - See Team activities by clicking a UI element
    Given I am logged in
    When I am on my team’s profile which id is 2
    Then I see Team activity page.


  Scenario: AC1 - See a list of Team activities by clicking a UI element
    Given I am logged in
    And I enter a valid Create Activity form with a team :
      | teamid | type     | startDate        | endDate          | description |
      | 2      | Training | 2099-03-25T14:00 | 2099-05-25T14:00 | something   |
      | 2      | Game     | 2099-04-25T14:00 | 2099-05-25T14:00 | Hi          |
    When I am on my team’s profile which id is 2
    Then I see a list of all activities for that team.

  Scenario: AC4 - Only see an edit button if I am the manager or coach of a team.
    Given I am logged in
    And I enter a valid Create Activity form with a team :
      | teamid | type     | startDate        | endDate          | description |
      | 2      | Training | 2099-03-25T14:00 | 2099-05-25T14:00 | something   |
      | 2      | Game     | 2099-04-25T14:00 | 2099-05-25T14:00 | Hi          |

    When I am on my team’s profile which id is 2
    And I am the Manager of the team
    Then I can see an edit activity button.






