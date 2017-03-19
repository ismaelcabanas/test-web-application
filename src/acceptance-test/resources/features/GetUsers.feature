Feature: Get users through REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to read users from the system

  Background:
    Given there are the next users in the system
      | username | password | roles        |
      | user1    | changeIt | Page1        |
      | user2    | changeIt | Page1, Page2 |
      | admin    | admin    | ADMIN        |
    And the web server is running on port 8082

  Scenario: Get user date with an admin user
    Given I want to get data for user user1
    When I use API for getting users with user admin and password admin
    Then the web server returns 200 status code

  Scenario: Get user date with an non admin user
    Given I want to get data for user user2
    When I use API for getting users with user user1 and password changeIt
    Then the web server returns 200 status code

  Scenario: Getting non existent user with a valid account
    Given I want to get data for user Ismael
    When I use API for getting users with user user1 and password changeIt
    Then the web server returns 404 status code

