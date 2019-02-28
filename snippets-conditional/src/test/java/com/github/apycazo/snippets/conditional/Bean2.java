package com.github.apycazo.snippets.conditional;

import org.springframework.stereotype.Component;

// requires the property to exist and be set to 'true'.
@Component
@RequiredProperty(key = "enabled.no", value = "true")
public class Bean2 extends GenericBean {
  // content on generic bean
}
