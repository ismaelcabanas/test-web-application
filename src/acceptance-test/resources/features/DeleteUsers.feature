Feature: Delete users through REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to delete users in the system

  Background:
    Given there are the next users in the system
      | username | password | roles        |
      | user1    | changeIt | Page1        |
      | user2    | changeIt | Page1, Page2 |
      | admin    | admin    | ADMIN        |
    And the web server is running on port 8082

  Scenario: Deleting user roles with an user with required permissions
    Given I want to delete user user1
    When I use API for deleting users with user admin and password admin
    Then the web server returns 204 status code

  Scenario: Deleting non existent user with an user with required permissions
    Give I want to delete user Ismael
    When I use API for deleting users with user admin and password admin
    Then the web server returns 404 status code

  Scenario: Deleting user with an invalid account
    Given I want to delete user user1
    When I use API for deleting users with user admin and password wrongpassword
    Then the web server returns 401 status code

  Scenario: Deleting user with an user without required permissions
    Given I want to delete user user1
    When I use API for deleting users with user admin and password admin
    Then the web server returns 204 status code