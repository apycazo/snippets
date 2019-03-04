package com.github.apycazo.snippets.spring.rest.data;

import lombok.Data;

@Data
public class ExceptionResult {

  private String message;
  private String exceptionClass;
}
