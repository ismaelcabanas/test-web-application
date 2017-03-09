Feature: Process request to web server
  As an evaluator
  I want request some pages to the web server started
  In order to test that the web server returns the page.

  Scenario: Request public page
    Given the web server is running on port 8002
    When I send a /login request to web server
    Then the web server returns Login resource
    And 200 status code

  Scenario: Request unknown page
    Given the web server is running on port 8005
    When I send a /unknownPage request to web server
    Then the web server returns Resource not found resource
    And 404 status code