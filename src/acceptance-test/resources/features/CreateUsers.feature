Feature: Create users trhough REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to create users in the system

  Background:
    Given there is the user admin/admin with Admin role in the system
    And the web server is running on port 8082
  Scenario: Creating user with a valid account
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
