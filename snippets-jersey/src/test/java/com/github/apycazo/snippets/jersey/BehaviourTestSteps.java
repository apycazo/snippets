package com.github.apycazo.snippets.jersey;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BehaviourTestSteps {

  ValidatableResponse response;
  int port = SocketUtils.findAvailableTcpPort();
  Server server = JerseyApp.configureServer(port);

  @When("I send a GET request to (.*)")
  public void send_GET_to(String path) {
    response = get(processPath(path)).then();
  }

  @Then("^the response has status code (\\d+)$")
  public void response_has_status(int status) {
    assertThat(response.extract().statusCode()).isEqualTo(status);
  }

  @Then("^the response path (.*) has value (.*)$")
  public void response_path_has_value(String path, String value) {
    String pathValue = response.extract().body().path(path);
    assertThat(pathValue).isEqualTo(value);
  }

  @Then("^log the response$")
  public void log_response() {
    log.info("Status: {}, Content: {}", response.extract().statusCode(), response.extract().body().asString());
  }

  @Before
  public void startServer() throws Exception {
    server.start();
  }

  @After
  public void stopServer() throws Exception {
    server.stop();
  }

  private String processPath (String path) {
    if (StringUtils.isEmpty(path) || path.startsWith("http:") || path.startsWith("https:")) {
      return path;
    } else if (path.startsWith("/")) {
      return "http://127.0.0.1:" + port + path;
    } else {
      return "http://127.0.0.1:" + port + "/" + path;
    }
  }

}
