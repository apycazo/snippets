package com.github.apycazo.snippets.spring.rest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class Settings {

  private String defaultResponseMessage = "test";
  private int serviceId = 10;
  private Author author = new Author();

  @Data
  private class Author {
    private String name;
    private String id;
  }
}
