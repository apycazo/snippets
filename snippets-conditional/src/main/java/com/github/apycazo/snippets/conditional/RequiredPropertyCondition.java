package com.github.apycazo.snippets.conditional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class RequiredPropertyCondition implements ConfigurationCondition {

  private final Logger log = LoggerFactory.getLogger(RequiredPropertyCondition.class);

  @Override
  public boolean matches(
    ConditionContext context, AnnotatedTypeMetadata metadata) {
    MultiValueMap<String, Object> attributes = metadata
      .getAllAnnotationAttributes(RequiredProperty.class.getName());
    if (attributes == null || !attributes.containsKey("key")) {
      log.warn("Key attribute not found, returning false");
      return false;
    }
    Environment environment = context.getEnvironment();
    String key = String.valueOf(attributes.getFirst("key"));
    if (!environment.containsProperty(key)) {
      Object raw = attributes.getFirst("matchOnMissingProperty");
      return raw instanceof Boolean ? (Boolean) raw : false;
    } else {
      // is a specific value required?
      String propValue = environment.getProperty(key);
      String requiredValue = String.valueOf(attributes.getFirst("value"));
      if (StringUtils.isEmpty(propValue)) {
        return StringUtils.isEmpty(requiredValue);
      } else if (StringUtils.isEmpty(requiredValue)) {
        return true;
      } else {
        return requiredValue.equals(propValue);
      }
    }
  }

  @Override
  public ConfigurationPhase getConfigurationPhase() {
    return ConfigurationPhase.REGISTER_BEAN;
  }
}
