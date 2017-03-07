Feature: Process request to web server
  As an evaluator
  I want request some pages to the web server started
  In order to test that the web server returns the page.

  Scenario: Request page1
    Given the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server returns Hello

  Scenario: Request page2
    Given the web server is running on port 8003
    When I send a /page2 request to web server
    Then the web server returns Hello

  Scenario: Request page3
    Given the web server is running on port 8004
    When I send a /page3 request to web server
    Then the web server returns Hello
