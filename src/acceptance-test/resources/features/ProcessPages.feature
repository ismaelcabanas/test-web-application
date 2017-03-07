Feature: Process request to web server
  As an evaluator
  I want request some pages to the web server started
  In order to test that the web server returns the page.

  Scenario: Request a page
    Given the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server returns Hello
