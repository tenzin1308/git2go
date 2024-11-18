package com.aexp.acq.go2.rest_interactions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.Interaction;
import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Deprecated(since = "2.0.0", forRemoval = true)
public class ListBranchesEndPoint extends Interaction {

  private static final Logger logger = LoggerFactory.getLogger(ListBranchesEndPoint.class);

  public ListBranchesEndPoint(String name) {
    super(name);
  }

  protected Object process(Object... vargs) {
    Map<String, String> tokens = new HashMap<>();
    tokens.put("github.base.url", (String)vargs[0]);
    tokens.put("github.repo", (String)vargs[1]);
    tokens.put("page", (String)vargs[2]);

    String target = BaseUtils.stringSubstitutor(App.instance().getProperty("github.api.get.all.branches"), tokens);

    logger.info("Target url -> {}", target);
    return get(target);
  }

}

