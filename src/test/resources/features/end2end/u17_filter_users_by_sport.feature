Feature: U17 - Filter Users by Sport

  Scenario: AC1 - Given I am on the search users page, when I select a sport from a list of sports known by the system, then only the users with the selected sport are displayed.
    Given I am logged in
    And I am being shown a user search form
    When I select a sport from a list of sports known by the system
    Then only the "user"s with the selected sport are displayed.

  Scenario: AC2 - Given I am on the search users page, when I select more than one sport from a list of sports known by the system, then all users with any of the selected sports are displayed.
    Given I am logged in
    And I am being shown a user search form
    When I select more than one sport from a list of sports known by the system, when filtering "user"s
    Then All "user"s with any of the selected sports are displayed

  Scenario: AC3 - Given I am on the search users page, when I deselect one or more sports from a list of sports known by the system, then the list of users updates according to the selected sports.
    Given I am logged in
    And I am being shown a user search form
    When I select more than one sport from a list of sports known by the system, when filtering "user"s
    And I deselect one or more sports from a list of sports known by the system, when filtering "user"s
    Then The list of "user"s updates according to the selected sports.

