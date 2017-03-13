Feature: Session timeout handling
  As an evaluator
  I want that the web server manages session timeout
  In order to the web server redirects me to login page after 2 inactivity minutes

  Background:
    Given there is the user admin/admin with Admin role in the system

  @ignored
  Scenario: Session timeout after inactivity period
    Given private resources /page1, /page2
    And session timeout is configured to 30 seconds
    And the web server is running on port 8002
    And I log in with admin/admin credentials
    When after 30 seconds I send a /page1 request to web server
    Then the web server redirects me to login page
