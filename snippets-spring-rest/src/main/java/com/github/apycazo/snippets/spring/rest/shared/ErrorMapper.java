package com.github.apycazo.snippets.spring.rest.shared;

import com.github.apycazo.snippets.spring.rest.data.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorMapper {

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleException(Exception e) {
    log.trace("Captured exception: {}", e.getMessage());
    ExceptionResult result = new ExceptionResult();
    result.setMessage(e.getMessage());
    result.setExceptionClass(e.getClass().getName());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
  }
}
