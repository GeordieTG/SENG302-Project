Feature: U30 - Edit Activity

  Scenario: AC1 - Edit button from view all activity page shows Edit Activity Form
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the view team activities page
    And I see the list of activities
    When I hit the edit activity button next to the activity entry in the list
    Then I see the edited activity form

  Scenario: AC2 - Edit button from team activities page shows Edit Activity Form
    Given I am logged in with a new user
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with team New Zealand National Team
    And I navigate to the view my teams activities page
#    And I am the manager or coach of the team
    When I hit the edit activity button next to the activity entry in the team activity list
    Then I see the edited activity form

  Scenario: AC3 - Successfully edit an activity without a team
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a valid Edit Activity form without a team
    Then I see the edited activity details without the team

  Scenario: AC3 - Successfully edit an activity with a team
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I navigate to the Edit Activity form
    When I enter a valid Edit Activity form with a team
    Then I see the edited activity details with the team

  Scenario: AC4 - Select activity type
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    When I navigate to the Edit Activity form
    Then I am able to select from a list of activity types

  Scenario: AC5 - Team is required
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with activity type that requires a team with no team
    Then I see an "teamError" error message saying "You must select a team for this activity type"

  Scenario: AC6 - Missing Activity Type
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with a missing activity type
    Then I see an "typeError" error message saying "You must select an activity type"

  Scenario: AC7 - Invalid Description
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with an invalid description
    Then I see an "descriptionError" error message saying "Description is invalid"


  Scenario: AC8 - No Start and End Time
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with missing Start Time and End Times
    Then I see an "startTimeError" error message saying "Start time is compulsory"
    And I see an "endTimeError" error message saying "End time is compulsory"

  Scenario: AC9 - End time before Start Time
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with a End Time before the Start Time
    Then I see an "endTimeBeforeStartTimeError" error message saying "End time is before start time"

  Scenario: AC10 - Start Time and End Time before Team Creation Time
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form without a team
    And I navigate to the Edit Activity form
    When I enter a Edit Activity form with a Start Time and End Time before the Team Creation Time
    Then I see an "startCreationTimeError" error message saying "Start time prior to team creation"
    And I see an "endCreationTimeError" error message saying "End time prior to team creation"


