Feature: U34 - Add activity statistics

  Scenario Outline: AC6 - Given I attempt to add a score with two matching and valid formats,
  when I attempt to submit the score, Then the score is persisted in the system.
    Given I logged in
    And I have an activity with the type <type>
    And I set up a lineup for the activity and click the save button
    When I attempt to submit a score with two matching and valid formats
    Then the score is persisted in the system
    Examples:
      | type   |
      | "Game" |

  Scenario Outline: AC7 - Given the activity type is a game or a friendly, When I view the add statistics page or element,
  then I can add the names of the person who scored and times in the activity when they scored.
    Given I logged in
    And I have an activity with the type <type>
    When I select the name of a person who scored
    And I input <time> as time of when they scored
    And I input <score> as how much they scored
    And I submit the form
    Then The player scored details are correctly persisted in the database, with the time, score, being <time>, <score>
    Examples:
      | type       | time | score |
      | "Game"     | 5    | 2     |
      | "Friendly" | 5    | 2     |

  Scenario Outline: AC8 - Given the activity type is a game or a friendly, When I view the add statistics page or element,
  I can record which player was substituted by whom and when.
    Given I logged in
    And I have an activity with the type <type>
    And I have at least two players in my team
    When I select the name of the substitute
    And I select the name of the substituted player
    And I input <time> as the minute when the substitution occurred
    And I submit the form
    Then The details of the substitution are persisted in the database, with time being <time>
    Examples:
      | type       | time |
      | "Game"     | 5    |
      | "Friendly" | 5    |


  Scenario Outline: AC10 - given I can interact with the add activity facts UI element, then I can record facts about
  that activity in the form of a fact description and an optional time when that fact happened.
    Given I logged in
    And I have an activity with the type <type>
    When I enter <description> as the fact description
    And I submit the facts form
    Then The details of the fact are persisted in the database, with <description> as the description
    Examples:
      | type       | description          |
      | "Game"     | "The sky is blue"    |
      | "Friendly" | "Oranges are orange" |



  Scenario Outline: AC6 - Given I attempt to add a score with two matching and invalid formats (too many point home
  score,
  when I attempt to submit the score, Then the score is persisted in the system.
    Given I logged in
    And I have an activity with the type <type>
    And I set up a lineup for the activity and click the save button
    When I attempt to submit a score with two matching and invalid formats
    Then data is not saved to database.
    Examples:
      | type   |
      | "Game" |
