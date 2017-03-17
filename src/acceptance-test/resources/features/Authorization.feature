Feature: Authorization in web application
  As an evaluator
  I want an authorization mechanism in the web server
  In order to only authorized users can access to private pages.

  Background:
    Given the next table of permissions
      | resource | roles        |
      | /page1   | Admin, Page1 |
      | /page2   | Page2        |
      | /page3   | Page3        |
    And there are the next users in the system
      | username | password | roles        |
      | user1    | changeIt | Page1        |
      | user2    | changeIt | Page1, Page2 |
      | admin    | admin    | Admin        |
    And private resources /page1, /page2, /page3
    And the web server is running on port 8002

  Scenario: User authenticated accesses to private resources without required permissions
    Given user1 logs in the system
    When sends a /page2 request to web server
    Then the web server returns Forbidden resource

  Scenario: User authenticated accesses to private with required permissions
    Given user1 logs in the system
    When sends a /page1 request to web server
    Then the web server returns PAGE1 resource

  Scenario: Admin user authenticated accesses to any private resources
    Given admin logs in the system
    When sends a /page2 request to web server
    Then the web server returns PAGE2 resource
    And sends a /page3 request to web server
    Then the web server returns PAGE3 resource
    When sends a /page1 request to web server
    Then the web server returns PAGE1 resource
