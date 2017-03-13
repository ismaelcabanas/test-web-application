Feature: Session logout
  As an evaluator
  I want to close an user session through a link in page
  In order to logout the web application and can to log in with other user

  Background:
    Given there is the user admin/admin with Admin role in the system

  @ignore
  Scenario: Logout from private resource
    Given private resources /page1
    Given the web server is running on port 8002
    And I log in with admin/admin credentials
    And I send a /page1 request to web server
    When I logout
    Then the web server redirects me to login page