package com.github.apycazo.snippets.jersey.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoService {

  @Value("${info.developer}")
  private String developer;

  public DemoData getDemoData(String message) {
    log.info("Developer: {}", developer);
    return DemoData.builder().message(message).build();
  }
}
