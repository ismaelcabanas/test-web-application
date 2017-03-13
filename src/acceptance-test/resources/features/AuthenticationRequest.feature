Feature: Authentication
  As an evaluator
  I want an authentication mechanism
  In order to only authenticated users can access to private resources

  Background:
    Given there is the user admin/admin with Admin role in the system

  Scenario: Authentication failed
    Given credentials ismael/wrongpassword
    And the web server is running on port 8002
    When I send try to login to web server
    Then the web server returns Unauthorized resource
    And 401 status code

  Scenario: Authentication failed
    Given credentials admin/admin
    And the web server is running on port 8002
    When I send try to login to web server
    Then the web server returns Home resource
    And 200 status code

  @ignore
  Scenario: Unauthenticated users cannot access to private resources
    Given private resource /page1
    And the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server redirects me to login page

  @ignore
  Scenario: Authenticated users can access to private resources
    Given private resource /page1
    And the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server redirects me to login page
    And if I login with user Ismael and password changeIt successfully
    Then the web server returns Hello Ismael, you are in PAGE1 resource

