package com.github.apycazo.snippets.spring.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Task {

  @Scheduled(cron = "${cli.cron}")
  public void scheduledAction () {
    log.info("Scheduler action run");
  }
}
