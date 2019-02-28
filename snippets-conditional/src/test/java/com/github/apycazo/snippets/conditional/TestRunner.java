package com.github.apycazo.snippets.conditional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class TestRunner {

  @Autowired(required = false) private Bean1 bean1;
  @Autowired(required = false) private Bean2 bean2;
  @Autowired(required = false) private Bean3 bean3;
  @Autowired(required = false) private Bean4 bean4;

  @Test
  public void bean_requiring_a_property_correctly_set_is_injected () {
    assertThat(bean1).isNotNull();
  }

  @Test
  public void bean_requiring_a_property_with_invalid_value_is_not_injected () {
    assertThat(bean2).isNull();
  }

  @Test
  public void bean_requiring_a_bean_that_is_instanced() {
    assertThat(bean3).isNotNull();
  }

  @Test
  public void bean_requiring_a_bean_to_be_missing() {
    assertThat(bean4).isNull();
  }
}
