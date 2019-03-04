package com.github.apycazo.snippets.spring.rest.controller;

import com.github.apycazo.snippets.spring.rest.config.Settings;
import com.github.apycazo.snippets.spring.rest.data.OperationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

  @Autowired
  private Settings settings;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public OperationResult<String> get() {
    OperationResult<String> result = OperationResult.<String>builder().data("DemoController.get").build();
    log.info("Result: {}", result);
    return result;
  }

  @GetMapping(path = "settings", produces = MediaType.APPLICATION_JSON_VALUE)
  public OperationResult<Settings> getSettings() {
    OperationResult<Settings> result = OperationResult.<Settings>builder().data(settings).build();
    log.info("Result: {}", result);
    return result;
  }

  @GetMapping(path = "params", produces = MediaType.APPLICATION_JSON_VALUE)
  public OperationResult<Map<String, String>> getParameters (@RequestParam Map<String,String> allRequestParams) {
    OperationResult<Map<String, String>> result = OperationResult.<Map<String, String>>builder().data(allRequestParams).build();
    log.info("Result: {}", result);
    return result;
  }

  @GetMapping(path = "param", produces = MediaType.APPLICATION_JSON_VALUE, params = "source")
  public OperationResult<String> echoRequiredParamValue(@RequestParam String src) {
    OperationResult<String> result = OperationResult.<String>builder().data(src).build();
    log.info("Result: {}", result);
    return result;
  }

  @PostMapping
  public OperationResult<List<String>> getRequestBody(@RequestBody List<String> values) {
    OperationResult<List<String>> result = OperationResult.<List<String>>builder().data(values).build();
    log.info("Result: {}", result);
    return result;
  }

  @GetMapping(path = "ex")
  public void throwError() {
    throw new RuntimeException("Failed on purpose");
  }
}
