package com.github.apycazo.snippets.conditional;

import org.springframework.stereotype.Component;

// requires the property to exist and be set to 'true'.
@Component
@RequiredProperty(key = "enabled.yes", value = "true")
public class Bean1 extends GenericBean {
  // content on generic bean
}
