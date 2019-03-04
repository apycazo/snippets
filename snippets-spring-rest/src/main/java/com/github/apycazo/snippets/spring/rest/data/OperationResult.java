package com.github.apycazo.snippets.spring.rest.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationResult<T> {

  @Builder.Default
  private String operationId = UUID.randomUUID().toString().substring(24);
  @Builder.Default
  private String message = "ok";
  @Builder.Default
  private boolean successful = true;
  @Builder.Default
  private T data = null;
  @Builder.Default
  private long timestamp = Instant.now().toEpochMilli();
}
