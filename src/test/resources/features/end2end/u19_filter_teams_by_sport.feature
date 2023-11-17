Feature: U19 - Filter Teams by Sport

  Scenario: AC1 - Given I am on the search teams page, when I select a sport from a list of sports known by the system, then only the teams with the selected sport are displayed.
    Given I am logged in
    And I am being shown a team search form
    When I select a sport from a list of sports known by the system
    Then only the "team"s with the selected sport are displayed.

  Scenario: AC2 - Given I am on the search teams page, when I select more than one sport from a list of sports known by the system, then all teams with any of the selected sports are displayed.
    Given I am logged in
    And I am being shown a team search form
    When I select more than one sport from a list of sports known by the system, when filtering "team"s
    Then All "team"s with any of the selected sports are displayed

  Scenario: AC3 - Given I am on the search teams page, when I deselect one or more sports from a list of sports known by the system, then the list of teams updates according to the selected sports.
    Given I am logged in
    And I am being shown a team search form
    When I select more than one sport from a list of sports known by the system, when filtering "team"s
    And I deselect one or more sports from a list of sports known by the system, when filtering "team"s
    Then The list of "team"s updates according to the selected sports.

  Scenario: AC4 - Given I am on the search teams page, when i click the "reset" button, then the list of teams refreshes without any filters or search applied.
    Given I am logged in
    And I am being shown a team search form
    When I click the Reset button
    Then The list of teams refreshes without any filters or search applied
