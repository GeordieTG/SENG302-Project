Feature: U32 - Create Formation

  Scenario: AC1 - Given I am on my team’s profile, when I click on a UI element to see all the team’s formations,
  then I see a list of all formations for that team.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    And I submit a valid formation form
    And I am on my first team’s profile
    When I hit the see all formations button
    Then I can see the teams formations

  Scenario: AC2 -  Given I am on my team’s formation page, when I click on a UI element to create a new line-up, then
  I am given an option to select a sport correlating to a graphical representation of a pitch (a simplified green or
  blue rectangle with white delimitation's and an indication of the goal / hook / scoring line is also accepted).
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    When I click the create formation button
    Then I am given an option to select a sports field

  Scenario: AC3 - When I am on a formation creation page, then I need to specify a number of players per sector on the
  pitch in the form of dash separated numbers starting from the back line up to the front line on the pitch
  (e.g., “1-4-3-3" for football, “1-1-3-3-3" for hockey).
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    When I click the create formation button
    Then I am given an option to select a formation


  Scenario: AC4 - Given I set up the number of players per sector, when the number of players per sector is invalid
  (I.e. does not respect the pattern of a number followed by a dash except for the last number), or is empty, then an
  error message tells me that the formation is invalid.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    When I enter a formation form with an invalid formation field
    Then I can see an error message saying that the formation is invalid


  Scenario: AC5 - When I have set up a formation pattern, then I see icons of players organised as described by the
  pattern on the graphical pitch.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    When I set up a formation
    Then I see icons of players organised as described by the pattern on the graphical pitch


  Scenario: AC6 - Given I have correctly set up a formation with the number of players per sector, when I click on the
  create formation button, then the formation is persisted in the system.
    Given I am logged in
    And I am on my first team’s profile
    And I hit the see all formations button
    And I click the create formation button
    When I enter a valid formation form
    Then the formation is persisted in the system

