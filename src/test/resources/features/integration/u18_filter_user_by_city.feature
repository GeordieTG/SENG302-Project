Feature: U18 - Filter User Profiles by City

  Scenario: AC1 - Given I am on the search profiles page showing me results from a search query,
  when I select a city from the list of cities collected from the profiles shown in the results,
  then only the profiles with the selected city are displayed.
    Given I logged in
    And There are 3 users with sport "Futsal" and city "Exotic"
    When I select a city to filter by
    Then only the users with the selected city are displayed.

  Scenario: AC2 - Given I am on the search profiles page, when I select more than one city from
  the list of cities collected from the profiles shown in the results, then all profiles with any
  of the selected cities are displayed.
    Given I logged in
    And There are 3 users with sport "Futsal" and city "Xadia"
    And There are 3 users with sport "Futsal" and city "Gondor"
    When I select more than one city to filter by
    Then all the users with the selected city are displayed.

  Scenario: AC3 - Given I am on the search profiles page, when I deselect one or more cities from
  the list of cities collected from the profiles shown in the results, then the list of profiles
  updates according to the selected cities.
    Given I logged in
    And There are 3 users with sport "Futsal" and city "Unique"
    And There are 3 users with sport "Futsal" and city "Place"
    When I deselect a city to filter by
    Then only the users with the selected city are displayed.

  Scenario: AC4 - Given I am on the search profile page, when I click the "reset" button,
  then all filters and search input are removed and the entire list of users are shown.
    Given I logged in
    When I click reset
    Then all the users are displayed.
