package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class BaseComponent {

  protected static final String owner = App.instance().getProperty("github.graphql.owner");
  protected static final String repo = App.instance().getProperty("github.graphql.repo");
  private static Logger logger = LoggerFactory.getLogger(BaseComponent.class);
  private String name = null;
  private String className = null;

  protected BaseComponent(String className) {
    if (className == null) {
      this.name = BaseUtils.getSimpleClassName(this.getClass());
      this.className = BaseUtils.getCanonicalName(this.getClass());
    }
    else {
      this.name = BaseUtils.getSimpleClassName(className);
      this.className = BaseUtils.getCanonicalName(className);
    }
  }

  public final Object execute(Object... vargs) {
    logger.info("Executing -> {}", BaseUtils.getSimpleClassName(name));

    Object obj = process(vargs);

    logger.info("Exiting -> {}", BaseUtils.getSimpleClassName(name));
    return obj;
  }

  abstract protected Object process(Object... vargs);


}
