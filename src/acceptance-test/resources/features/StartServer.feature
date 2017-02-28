Feature: Start Web Server
  As an evaluator
  I want start a web server
  In order to initiate the technical test to the candidate

  Scenario: Starting Web Server
    When I start the web server on 8000 port
    Then the web server is up