Feature: Session handling
  As an evaluator
  I want that the web server manages user sessions
  In order to navigate for the site avoiding log in each request

  Background:
    Given there is the user admin/admin with Admin role in the system

  Scenario: Create user session when authenticate
    Given private resources /page1, /page2
    And the web server is running on port 8002
    And I log in with admin/admin credentials
    When I send a /page1 request to web server
    Then the web server returns PAGE1 resource
    When I send a /page2 request to web server
    Then the web server returns PAGE2 resource