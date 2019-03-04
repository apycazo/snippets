package com.github.apycazo.snippets.spring.rest.shared;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String id = UUID.randomUUID().toString().substring(24);
    MDC.put("cid", id);
    log.info("Request [{}]: {} {} (Handled by: {})",
      id, request.getMethod(), request.getRequestURL().toString(), handler.getClass().getName());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    String id = MDC.get("cid");
    log.info("Request [{}] has been handled (status: {})", id, response.getStatus());
  }
}
