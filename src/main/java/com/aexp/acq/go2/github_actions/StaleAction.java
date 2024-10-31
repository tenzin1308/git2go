package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.rest_interactions.GetBranchesEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaleAction extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(StaleAction.class);

  public StaleAction(String name) {
    super(name);
  }

  @Override
  protected Object process(Object... vargs) {
    logger.info("Processing StaleAction");
    GetBranchesEndPoint endPoint = new GetBranchesEndPoint("com.aexp.acq.go2.rest_interactions.GetBranchesEndPoint");
    return endPoint.execute(App.instance().getProperty("GITHUB_BASE_URL"), "132", "af");
  }


}
