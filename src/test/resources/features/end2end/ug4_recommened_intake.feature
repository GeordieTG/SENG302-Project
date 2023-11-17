Feature: UG1 - Recommended Intake

  Scenario: AC1 - Given I am logged in, When I am on my profile page, Then I can see a dedicated UI element to set my calorie intake preference
  {Bulk, Maintain, Cut} (Default maintain).
    Given I have logged in as user 1
    When I am on my profile page
    Then I can see a dedicated UI element to set my calorie intake preference

  Scenario Outline: AC1.1 - Given I am on my profile page, When I change my calorie intake preference, Then I can see a dedicated UI element telling me it has changed.
    Given I am logged in as team 1
    And I am on my profile page
    When I change my calorie intake preference to <preference>
    Then I can see a dedicated UI element telling me it has changed.
    Examples:
      | preference |
      | "bulk"     |
      | "maintain" |
      | "cut"      |