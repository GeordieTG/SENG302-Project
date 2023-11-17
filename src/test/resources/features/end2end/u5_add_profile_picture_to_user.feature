Feature: U5 - Add a profile picture to user

  Scenario: AC1 - Given I am on my user profile page, when I hit the “edit profile picture” button, then a file picker is shown.
    Given I am logged in
    And I am on my profile page
    When I hit the edit profile picture button
    Then I see a file picker

  Scenario: AC2 - When I submit a file of the right type and size, then my profile picture is updated.
    Given I am logged in
    And  I choose a new profile picture
    When I submit a file of the right type and size
    Then my profile picture is updated.

  Scenario: AC3 - given I choose a new profile picture, when I submit a file that is not either a png, jpg or svg, then my profile picture is not updated.
    Given I am logged in
    And I choose a new profile picture
    When I submit a file that is not either a png, jpg or svg
    Then My profile picture is not updated

  Scenario: AC4 - Given I choose a new profile picture, when I submit a valid file with a size of more than 10MB, then my profile picture is not updated.
    Given I am logged in
    And I choose a new profile picture
    When I submit a valid file with a size of more than 10MB
    Then My profile picture is not updated

  Scenario: AC5 -Given I am anywhere in the system where I can see my profile, when I see my profile details, then I see my profile picture or a default picture if none is set.
    Given I am logged in
    And I am on my profile page
    Then I see my profile picture
