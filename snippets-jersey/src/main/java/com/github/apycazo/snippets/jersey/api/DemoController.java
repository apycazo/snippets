package com.github.apycazo.snippets.jersey.api;

import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Path("demo")
public class DemoController {

  @Autowired
  private DemoService demoService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> info() {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now().toEpochMilli());
    return response;
  }

  @GET
  @Path("spring")
  @Produces(MediaType.APPLICATION_JSON)
  public DemoData demoInfo() {
    return demoService.getDemoData("test");
  }
}
