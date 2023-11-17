# All validation has already been tested for edge cases. This is just to ensure that the correct error messages are displayed.

Feature: U27 - Create Activity

  Background:
    Given I am logged in

  Scenario: AC1 - Activity button shows Create Activity Form
    When I hit the Activity button
    Then I am on the Create Activity form

  Scenario: AC2 - Successfully create an activity without a team
    And I navigate to the Create Activity form
    When I enter a valid Create Activity form without a team
    Then I see the activity details without the team

  Scenario: AC2 - Successfully create an activity with a team
    And I navigate to the Create Activity form
    When I enter a valid Create Activity form with a team
    Then I see the activity details with the team

  Scenario: AC3 - Select Team
    Given I am logged in
    And I have created a team "Bishops"
    And I have created a team "Bulldogs"
    When I navigate to the Create Activity form
    Then I am able to select from a list of my teams that I coach or manage

  Scenario: AC4 - Select activity type
    When I navigate to the Create Activity form
    Then I am able to select from a list of activity types

  Scenario: AC5 - Team is required
    And I navigate to the Create Activity form
    When I enter a Create Activity form with activity type that requires a team with no team
    Then I see a "teamError" error message saying "You must select a team for this activity type"

  Scenario: AC6 - Missing Activity Type
    And I navigate to the Create Activity form
    When I enter a Create Activity form with a missing activity type
    Then I see a "typeError" error message saying "You must select an activity type"

  Scenario: AC7 - Invalid Description
    And I navigate to the Create Activity form
    When I enter a Create Activity form with an invalid description
    Then I see a "descriptionError" error message saying "Description is invalid"

  Scenario: AC8 - No Start and End Time
    And I navigate to the Create Activity form
    When I enter a Create Activity form with missing Start Time and End Times
    Then I see a "startTimeError" error message saying "Start time is compulsory"
    And I see a "endTimeError" error message saying "End time is compulsory"

  Scenario: AC9 - End time before Start Time
    And I navigate to the Create Activity form
    When I enter a Create Activity form with a End Time before the Start Time
    Then I see a "endTimeBeforeStartTimeError" error message saying "End time is before start time"

  Scenario: AC10 - Start Time and End Time before Team Creation Time
    And I navigate to the Create Activity form
    When I enter a Create Activity form with a Start Time and End Time before the Team Creation Time
    Then I see a "startCreationTimeError" error message saying "Start time prior to team creation"
    And I see a "endCreationTimeError" error message saying "End time prior to team creation"

    #Not an actual AC, just makes sense to test the same validation as edit activity
  Scenario: AC4 - Given I am on the create activity form, when I edit the address and I don't
  specify all required fields (I.e. address line 1, city, postcode and country),
  then an error message tells me these two fields are mandatory.
    Given I am logged in
    When I hit the Activity button
    And I enter a "create" Activity form with no first address line, no city, no postcode, and no country
    Then I can see an error message saying that I must enter each of these fields