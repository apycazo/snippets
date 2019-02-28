package com.github.apycazo.snippets.conditional;

import org.springframework.stereotype.Component;

// requires the property to exist and be set to 'true'.
@Component
@RequiredBean(beanClass = Bean1.class, isFound = false)
public class Bean4 extends GenericBean {
  // content on generic bean
}
