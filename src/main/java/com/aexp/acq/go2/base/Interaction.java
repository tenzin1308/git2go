package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class Interaction {

  private static Logger logger = LoggerFactory.getLogger(Interaction.class);
  private String name = null;

  public Interaction(String className) {
    if (className == null) {
      this.name = BaseUtils.getSimpleClassName(this.getClass());
    }
    else {
      this.name = BaseUtils.getSimpleClassName(className);
    }
  }

  public final Object execute(Object... vargs) {
    logger.info("Executing Interaction -> {}", BaseUtils.getSimpleClassName(name));

    Object obj = process(vargs);

    logger.info("Exiting Interaction -> {}", BaseUtils.getSimpleClassName(name));
    return obj;
  }

  abstract protected Object process(Object... vargs);

}
