package com.github.apycazo.snippets.basic;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileManagement {

  public static void main(String[] args) throws Exception {
    String filename = "application.properties";
    log.info("Loading file: {}", filename);
    URL url = FileManagement.class.getClassLoader().getResource(filename);
    Path path = Paths.get(url.toURI());
    String content = new String(Files.readAllBytes(path));
    log.info("Content: {}", content);
  }
}
