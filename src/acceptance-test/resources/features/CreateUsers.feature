Feature: Create users trhough REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to create users in the system

  Background:
    Given there are the next users in the system
      | username | password | roles        |
      | user1    | changeIt | Page1        |
      | user2    | changeIt | Page1, Page2 |
      | admin    | admin    | Admin        |
    And the web server is running on port 8082

  Scenario: Creating user with and user with required permissions
    Given I want create user with name Ismael
    And with password changeIt
    And with roles Admin, Page1
    When I use API for creating users with user admin and password admin
    Then the web server returns 201 status code

  Scenario: Creating user with an invalid account
    Given I want create user with name Ismael
    And with password changeIt
    And with roles Admin, Page1
    When I use API for creating users with user admin and password wrongpassword
    Then the web server returns 401 status code

  Scenario: Creating user with an user without required permissions
    Given I want create user with name Ismael
    And with password changeIt
    And with roles Admin, Page1
    When I use API for creating users with user user1 and password changeIt
    Then the web server returns 403 status code