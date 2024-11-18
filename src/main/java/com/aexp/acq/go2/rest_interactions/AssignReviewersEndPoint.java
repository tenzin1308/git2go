package com.aexp.acq.go2.rest_interactions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.RestInteraction;
import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AssignReviewersEndPoint extends RestInteraction {

  private static final Logger logger = LoggerFactory.getLogger(AssignReviewersEndPoint.class);

  public AssignReviewersEndPoint(String name) {
    super(name);
  }

  @Override
  protected Object process(Object... vargs) {
    Map<String, String> tokens = new HashMap<>();
    tokens.put("github.base.url", (String)vargs[0]);
    tokens.put("github.repo", (String)vargs[1]);
    tokens.put("pull_number", (String)vargs[2]);
    String body = (String)vargs[3];

    String target = BaseUtils.stringSubstitutor(App.instance().getProperty("github.api.request.reviewers"), tokens);
    return post(target, body);
  }

}
