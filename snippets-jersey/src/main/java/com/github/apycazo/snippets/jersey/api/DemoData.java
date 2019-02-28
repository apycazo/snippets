package com.github.apycazo.snippets.jersey.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoData {

  @Builder.Default
  private String message = "";
  @Builder.Default
  private Long timestamp = Instant.now().toEpochMilli();
}
