package com.github.apycazo.snippets.spring.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class CommandLineApp {

  public static void main(String[] args) {
    log.info("Starting spring application");
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(Config.class);
    ctx.refresh();
  }
}
