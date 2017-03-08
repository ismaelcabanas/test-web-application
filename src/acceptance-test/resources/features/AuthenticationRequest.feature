Feature:
  As an evaluator
  I want an authentication mechanism
  In order to only authenticated users can access to private resources

  Scenario: Unauthenticated users cannot access to private resources
    Given private resource /page1
    And the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server redirects me to login page

  Scenario: Authenticated users can access to private resources
    Given private resource /page1
    And the web server is running on port 8002
    When I send a /page1 request to web server
    Then the web server redirects me to login page
    And I log in with user Ismael and password changeIt successfully
    Then the web server returns Hello Ismael, you are in PAGE1

