Feature: U38.1 - Create Club

  Scenario: AC1 - Given I am logged in, When I click a dedicated UI element to create a club, Then
  I am presented with a form to create a club.
    Given I am logged in
    When I click a UI element to create a club
    Then I am presented with a club creation form

  Scenario: AC2 - Given I click on the dedicated UI element to create a club, When I am presented with a Create Club
  Form, Then I must specify a name, sport type and a location city and country being compulsory(optional. address
  line 1, address line 2, suburb, postcode).
    Given I am logged in
    When I click a UI element to create a club
    Then I am prompted to fill the required fields

  # Yet to be implemented
  Scenario: AC2 - When creating a club, I can add an optional logo. If none is given, a generic club image is used.

  Scenario: AC6 - Switched select dropdown to be readonly when the team has a sport
    Given I am logged in
    When I am on the edit team page of a team, that has a club
    Then I cannot edit the sport

  Scenario: AC8 - Given I am creating a Club, When I input an empty name, Then I see an error message telling me that
  the input is invalid
    Given I am logged in
    And I click a UI element to create a club
    When I fill the form with an empty "name"
    Then I see an error message for the "name" input

  Scenario: AC9 - Given I am creating a Club, When I input an empty city, Then I see an error
  message telling me that the input is invalid
    Given I am logged in
    And I am creating a club
    When I fill the form with an empty "city"
    Then I see an error message for the "city" input

  Scenario: AC10 - Given I am creating a Club, When I input an empty sport, Then I see an error
  message telling me that the input is invalid
    Given I am logged in
    And I am creating a club
    When I fill the form with an empty "sport"
    Then I see an error message for the "sport" input

  Scenario: AC11 - Given I am creating a Club, When I input an empty country, Then I see an error
  message telling me that the input is invalid
    Given I am logged in
    And I am creating a club
    When I fill the form with an empty "country"
    Then I see an error message for the "country" input
