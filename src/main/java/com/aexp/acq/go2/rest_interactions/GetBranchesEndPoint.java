package com.aexp.acq.go2.rest_interactions;

import com.aexp.acq.go2.base.Interaction;
import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBranchesEndPoint extends Interaction {

  private static final Logger logger = LoggerFactory.getLogger(GetBranchesEndPoint.class);

  public GetBranchesEndPoint(String name) {
    super(name);
  }

  protected Object process(Object... vargs) {
    String url = (String)vargs[0];
    String protectedBranches = (String)vargs[1];
    String currentPage = (String)vargs[2];

    String target = String.format("%s/branches?protected=%s&page=%s", url, protectedBranches, currentPage);

    logger.info("Target url -> {}", target);
    return get(target);
  }

}

