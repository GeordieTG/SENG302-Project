Feature: U22 - Update password

  Scenario: AC1 - Edit form is shown with three text fields, old, new and retype password
    Given I am logged in
    And I navigate to the edit user profile page
    When I click on the change password button
    Then I see a change password form

  Scenario: AC2 - Old password doesnt match current password
    Given I am logged in
    And I navigate to the edit user profile page
    And I click on the change password button
    And I see a change password form
    When I enter the old password and it is different to the password in the database
    Then I see an "oldPasswordError" error saying "Password is incorrect"

  Scenario: AC3 - New and retype passwords do not match
    Given I am logged in
    And I navigate to the edit user profile page
    And I click on the change password button
    And I see a change password form
    When I enter different new and retype passwords
    Then I see an "passwordMatchError" error saying "Passwords do not match"

  Scenario: AC4 - Enter a weak password
    Given I am logged in
    And I navigate to the edit user profile page
    And I click on the change password button
    And I see a change password form
    When I enter a weak password
    Then I see an "weakPasswordError" error saying "Password is too weak. Please follow the rules above"


