Feature: U32 - Create Formation

  Scenario: AC4 - Given I set up the number of players per sector, when the number of players per sector is invalid
  (I.e. does not respect the pattern of a number followed by a dash except for the last number), or is empty, then
  an error message tells me that the formation is invalid.
    Given I logged in
    When I enter a formation form with an invalid formation
    Then An error message tells me that the formation is invalid

  Scenario: AC4 - Given I set up the number of players per sector, when field is left blank, then an error message tells
  me that the field is invalid
    Given I logged in
    When I enter a formation form with an invalid field
    Then An error message tells me that the field is invalid


  Scenario: AC5  When I have set up a formation pattern, then I see icons of players organised as described by the pattern on the graphical pitch.
    Given I logged in
    When I create a formation for my team
    Then I can view my formation graphically

  Scenario: AC6 - Given I have correctly set up a formation with the number of players per sector, when I click on the
  create formation button, then the formation is persisted in the system.
    Given I logged in
    When I create a formation for my team
    Then my formation is persisted in the database