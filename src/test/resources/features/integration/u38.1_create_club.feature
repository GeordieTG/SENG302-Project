Feature: U38.1 - Create Club

  Scenario: AC1 - Given I am logged in, When I click a dedicated UI element to create a club, Then
  I am presented with a form to create a club.
    Given I logged in
    When I click a dedicated UI element to create a club
    Then I am presented with a form to create a club

  Scenario: AC2 - Given I click on the dedicated UI element to create a club, When I am presented
  with a Create Club Form, Then I must specify a name, sport type and a location city and country
  being compulsory(optional. address line 1, address line 2, suburb, postcode).
  I can add an optional logo. If none is given, a generic club image is used.
    Given I logged in
    And I click a dedicated UI element to create a club
    When I am presented with a form to create a club
    Then I must specify a name, sport type, city and country
    And I can optionally add an address line 1, address line 2, suburb and postcode
    And I can add an optional logo
    And If none is given, a generic club image is used

  Scenario: AC4 - Given I am creating  When I go to add teams to my club, then
  I am unable to add a team that is already joined another club.
    Given I logged in
    And There are two teams I am a manager of
    And I click a dedicated UI element to create a club
    When I am presented with a form to create a club
    Then only the team without a club is available

  Scenario: AC7 - Given I create a club, when the club is created, then I become the club owner
    Given I logged in
    When The club is created
    Then I become the club owner

  Scenario: AC8 - Given I am creating a Club, When I input an empty name, Then I see an error
  message telling me that the input is invalid
    Given I logged in
    When I input an empty name
    Then I see an error message telling me that the input is invalid

  Scenario: AC9 - Given I am creating a Club, When I input an empty city, Then I see an error
  message telling me that the input is invalid
    Given I logged in
    When I input an empty city
    Then I see an error message telling me that the input is invalid

  Scenario: AC10 - Given I am creating a Club, When I input an empty sport, Then I see an error
  message telling me that the input is invalid
    Given I logged in
    When I input an empty sport
    Then I see an error message telling me that the input is invalid

  Scenario: AC11 - Given I am creating a Club, When I input an empty country, Then I see an error
  message telling me that the input is invalid
    Given I logged in
    When I input an empty country
    Then I see an error message telling me that the input is invalid