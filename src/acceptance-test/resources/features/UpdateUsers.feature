Feature: Update users through REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to modify users in the system

  Background:
    Given there is the user admin/admin with Admin role in the system

  Scenario: Updating user data with a valid account
    Given the web server is running on port 8082
    And there is an user in the system called Ismaelo with role Page1
    And I want update his name to Ismael and to add role Admin
    When I use API for updating users with user admin and password admin
    Then the web server returns 200 status code

  Scenario: Updating user data with an invalid account
    Given the web server is running on port 8082
    And there is an user in the system called Ismaelo with role Page1
    And I want update his name to Ismael and to add role Admin
    When I use API for updating users with user admin and password wrongpassword
    Then the web server returns 401 status code
