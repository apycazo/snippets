package com.github.apycazo.snippets.spring.rest.config;

import com.github.apycazo.snippets.spring.rest.shared.LogInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigurationAdapter implements WebMvcConfigurer {

  private String corsPattern = "/";
  private String allowedOrigins = "*";

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
      // path extensions will always be used when present
      .favorPathExtension(true)
      // Note that a browser might send a default xml header, which would override this
      .defaultContentType(MediaType.APPLICATION_JSON)
      // Extensions we allow to use (note that using xml requires dependency on 'jackson-dataformat-xml')
      .mediaType("xml", MediaType.APPLICATION_XML)
      .mediaType("json", MediaType.APPLICATION_JSON);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping(corsPattern).allowedOrigins(allowedOrigins);
  }

}
