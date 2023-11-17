Feature: U28 - View My Activities

  Scenario: AC1 - See All my Activities by clicking a UI element
    Given I logged in
    And I am a manager team Bla Bla
    And I have created the activities for team Bla Bla:
      | type     | startDate        | endDate          | description |
      | Training | 2099-03-25T14:00 | 2099-05-25T14:00 | something   |
      | Game     | 2099-04-25T14:00 | 2099-05-25T14:00 | Hi          |
    When I get on a my activities page
    Then I see my activity page with the activities for Bla Bla

  Scenario: AC1 - See All my Activities by clicking a UI element
    Given I logged in
    And I am a manager team Bla Bla
    And I have created the activities for team Bla Bla:
      | type | startDate | endDate | description |
    When I get on a my activities page
    Then I see my activity page with no activities

  Scenario: AC4 - See a List of activities in time order
    Given I logged in
    And I am a manager team Bla Bla
    And I have created the activities for team Bla Bla:
      | type     | startDate        | endDate          | description |
      | Training | 2099-03-25T14:00 | 2099-05-25T14:00 | something   |
      | Game     | 2099-03-25T13:00 | 2099-05-25T14:00 | Hi          |
    When I get on a my activities page
    Then I see my activity page with the activities for Bla Bla
    And I see the activities are in ascending order by time