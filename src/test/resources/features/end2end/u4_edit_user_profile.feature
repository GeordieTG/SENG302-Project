Feature: U4 - Edit user profile.

  Scenario: AC1 - Given I am on my user profile page, when I hit the edit button,
  then I see the edit profile form with all my details prepopulated except the passwords.
    Given I have logged in
    And I am on my profile page
    When I hit the edit button
    Then I see the edit profile form with all my details prepopulated

  Scenario: AC2 - Given I am on the edit profile form, and I enter valid values for my first name, last name,
  email address, and date of birth, when I hit the save button, then my new details are saved.
    Given I have logged in as user 11
    And I am on my profile page
    And I hit the edit button
    And I enter a valid edit profile form
    When I hit the save button
    Then My new details are saved

  Scenario: AC3 - Given I am on the edit profile form, and I enter invalid values
  (i.e. empty strings or non-alphabetical characters) for my first name and/or last name, when I hit the save button,
  then an error message tells me these fields contain invalid values.
    Given I have logged in as user 1
    And I am on my profile page
    And I hit the edit button
    And I enter invalid values for my names
    When I hit the save button
    Then An error message tells me there are invalid values

  Scenario: AC5 - Given I am on the edit profile form, and I enter a date of birth for someone younger than 13 years old,
  when I hit the save button, then an error message tells me I cannot register into the system because I am too young.
    Given I have logged in as user 1
    And I am on my profile page
    And I hit the edit button
    And I enter a date of birth for someone younger than 13 years old
    When I hit the save button
    Then An error message tells me the date of birth is invalid

  Scenario: AC6 - Given I am on the edit profile form, and I enter a new email address that already exists in the system,
  when I hit the save button, then an error message tells me the email address is already registered.
    Given I have logged in as user 1
    And I am on my profile page
    And I hit the edit button
    And I enter a new email address that already exists in the system
    When I hit the save button
    Then An error message tells me the email is already registered

  Scenario: AC7 - Given I am on the edit profile form, when I hit the cancel button, I come back to my profile page,
  and no changes have been made to my profile.
    Given I have logged in as user 1
    And I am on my profile page
    And I hit the edit button
    When I hit the cancel button
    Then I come back to my profile page with no details changed