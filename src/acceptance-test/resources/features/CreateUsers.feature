Feature: Create users trhough REST API
  As an evaluator
  I want that the web server expose a REST API
  In order to create users in the system

  Background:
    Given the web server is running on port 8002

  Scenario: Creating user with roles
    Given I want create user with name Ismael
    And with password changeIt
    And with roles Admin, Page1
    When I use API for creating users
    Then the web server returns 201 status code
