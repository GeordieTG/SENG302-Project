Feature: U31 - Add Activity Location

  Scenario: AC1 - Given I am on the create activity form, when I want to add a team’s location, then I can specify a
  full address, i.e. up to two lines of free text (e.g., line 1: 114 Ilam Rd, line 2: Ilam), a suburb, a city, a
  postcode, and a country.
    Given I am logged in
    When I hit the Activity button
    And I hit the next button
    Then I can see fields to enter the location details

  Scenario: AC2 - Given I am on the edit activity form of a personal or team activity I manage or coach,
  when I want to add a team’s location, then I can specify a full address, i.e. up to two lines of free text
  (e.g., line 1: 114 Ilam Rd, line 2: Ilam), a suburb, a city, a postcode, and a country.
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I navigate to the Edit Activity form
    When I am the Manager of the team
    And I hit the next button
    Then I can see fields to enter the location details

  Scenario Outline: AC3 - Given I edit the location (a.k.a. address) of an activity, when I edit the address,
  then I must specify at least the first address line, the city, the postcode and the country
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I navigate to the Edit Activity form
    When I enter an Activity form with no <field>
    Then I see an <error> error message saying <message>
    Examples:
      | field      | error                | message                   |
      | "postcode" | "validPostcodeError" | "Please enter a postcode" |
      | "country"  | "validCountryError"  | "Please enter a country"  |
      | "city"     | "validCityError"     | "Please enter a city"     |
      | "address"  | "validAddressError"  | "Please enter an address" |


  Scenario: AC4 - Given I edit the location (a.k.a. address) of an activity, when I edit the address and I don't
  specify all required fields (I.e. address line 1, city, postcode and country),
  then an error message tells me these two fields are mandatory.
    Given I am logged in
    And I navigate to the Create Activity form
    And I enter a valid Create Activity form with a team
    And I navigate to the Edit Activity form
    And I enter a "edit" Activity form with no first address line, no city, no postcode, and no country
    Then I can see an error message saying that I must enter each of these fields
