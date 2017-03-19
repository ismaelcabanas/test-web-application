Feature: Update users through REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to modify users in the system

  Background:
    Given there are the next users in the system
      | username | password | roles        |
      | user1    | changeIt | Page1        |
      | user2    | changeIt | Page1, Page2 |
      | admin    | admin    | ADMIN        |
    And the web server is running on port 8082

  Scenario: Updating user roles with an user with required permissions
    Given I want to update roles to ADMIN for user user1
    When I use API for updating users with user admin and password admin
    Then the web server returns 200 status code

  Scenario: Updating non existent user with an user with required permissions
    Given I want to update roles to ADMIN for user Ismael
    When I use API for updating users with user admin and password admin
    Then the web server returns 404 status code

  Scenario: Updating user data with an invalid account
    Given I want to update roles to ADMIN for user user1
    When I use API for updating users with user admin and password wrongpassword
    Then the web server returns 401 status code

  Scenario: Updating user with un user without required permissions
    Given I want to update roles to ADMIN for user user1
    When I use API for updating users with user user1 and password changeIt
    Then the web server returns 403 status code