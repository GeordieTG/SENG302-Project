Feature: U13 - Search teams

  Scenario Outline: AC1 - Given I am on any page, when I hit the “search teams” button, then I see a search form.
    Given I am logged in
    And I am on the <page> page
    When I hit the Search Teams button
    Then I see a search form
    Examples:
      | page          |
      | "viewTeams"   |
      | "home"        |
      | "createTeam"  |
      | "profilePage" |

  Scenario: AC2 - When i submit a search string of at least 3 characters, I see a list of teams which match the search query
    Given I am logged in
    And I am being shown a team search form
    When I submit the search query "National"
    Then I see a list of teams which match the search query "National"

  Scenario: AC5 - Given I see the list of all matching teams, when there is more than one matching team, then teams are sorted alphabetically.
    Given I am logged in
    And I am being shown a team search form
    When I submit the search query "National"
    Then I see a list of teams which match the search query "National"
    And The teams are sorted correctly

  Scenario: AC7 - When i enter a query string that is too short, I see an error message
    Given I am logged in
    And I am being shown a team search form
    When I submit the search query "Ja"
    Then I see an error message telling me that my query is too short

  Scenario: AC8 - Given I see the list of all matching teams, when there are no results, then a message explicitly tells me there are no results.
    Given I am logged in
    And I am being shown a team search form
    When I submit the search query "asdsadsads"
    Then I see a message telling me there are no results
