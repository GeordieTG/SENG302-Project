Feature: U17 - Filter User Profiles by Sport

  Scenario: AC1 - Given I am on the search profiles page, when I select a
  sport from a list of sports known by the system, then only the profiles
  with the selected sport are displayed.
    Given I logged in
    And There are 3 users with sport "Golf" and city "Albany"
    When I select a sport to filter by
    Then only the users with the selected sport are displayed.

  Scenario: AC2 - Given I am on the search profiles page, when I select more than one sport from
  a list of sports known by the system, then all profiles with any of the selected sports are
  displayed.
    Given I logged in
    And There are 3 users with sport "Running" and city "Albany"
    And There are 3 users with sport "Futsal" and city "Albany"
    When I select more than one sport to filter by
    Then all the users with the selected sport are displayed.

  Scenario: AC3 - Given I am on the search profiles page, when I deselect one or more sports
  from a list of sports known by the system, then the list of profiles updates according to the selected sports.
    Given I logged in
    And There are 3 users with sport "Hockey" and city "Albany"
    And There are 3 users with sport "Football" and city "Albany"
    When I deselect a sport to filter by
    Then only the users with the selected sport are displayed.