Feature: Basic controller is available and responds to requests
  Scenario: Service responds to basic get requests
    When I send a GET request to /demo/spring
    Then log the response
    And the response has status code 200
    And the response path message has value test

