package com.github.apycazo.snippets.conditional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class RequiredBeanCondition implements ConfigurationCondition {

  private final Logger log = LoggerFactory.getLogger(RequiredBeanCondition.class);

  @Override
  public ConfigurationPhase getConfigurationPhase() {
    return ConfigurationPhase.REGISTER_BEAN;
  }

  @Override
  public boolean matches(
    ConditionContext context, AnnotatedTypeMetadata metadata) {
    MultiValueMap<String, Object> attributes = metadata
      .getAllAnnotationAttributes(RequiredBean.class.getName());
    if (!attributes.containsKey("beanClass")) {
      log.warn("beanClass attribute not found, returning false");
      return false;
    }

    Class<?> beanClass = (Class<?>)attributes.getFirst("beanClass");
    boolean isRequired = (boolean)attributes.getFirst("isFound");
    try {
      context.getBeanFactory().getBean(beanClass);
      if (beanClass.isAnnotationPresent(RequiredProperty.class)) {
        // check if the bean fulfills the annotation condition
        RequiredPropertyCondition cond = new RequiredPropertyCondition();
        StandardAnnotationMetadata beanMetadata =
          new StandardAnnotationMetadata(beanClass);
        boolean isConditionMatched = cond.matches(context, beanMetadata);
        return isConditionMatched == isRequired;
      } else {
        return isRequired;
      }
    } catch (NoSuchBeanDefinitionException e) {
      // bean not found return true it such was the condition
      return !isRequired;
    }
  }
}
