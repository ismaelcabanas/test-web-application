Feature: Get users through REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to read users from the system

  Background:
    Given there is the user admin/admin with Admin role in the system
    And the web server is running on port 8082

  Scenario: Get user date with a valid account
    Given there is an user in the system called Ismael with role Page1
    And And I want to get data for user Ismael
    When I use API for getting users with user admin and password admin
    Then the web server returns 200 status code

  Scenario: Getting non existent user with a valid account
    Given And I want to get data for user Ismael
    When I use API for getting users with user admin and password admin
    Then the web server returns 404 status code

  Scenario: Updating user data with an invalid account
    Given there is an user in the system called Ismael with role Page1
    And And I want to get data for user Ismael
    When I use API for getting users with user admin and password wrongpassword
    Then the web server returns 401 status code
