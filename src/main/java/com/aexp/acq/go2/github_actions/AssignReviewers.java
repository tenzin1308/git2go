package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
import com.aexp.acq.go2.rest_interactions.AssignReviewersEndPoint;
import com.aexp.acq.go2.rest_interactions.GetPullRequestEndPoint;
import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignReviewers extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(AssignReviewers.class);
  private Document configDoc = BaseUtils.getResouceAsDocument(AssignReviewers.class, App.instance().getProperty("reviewers.config.path"), "assign_reviewers_config");

  protected AssignReviewers(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    // if config doc is null, do nothing
    if (configDoc == null) {
      logger.error("Config document not found. Exiting.");
      return null;
    }
    String baseBranch = invokeGetPullRequestEndPoint();
    if (baseBranch != null) {
      int statusCode = invokeAssignReviewersEndPoint(configDoc.getContent("$.assign_reviewers_config[branch=%].request_reviewers", false, false, baseBranch).getPrettyPrintJson());
      if (statusCode == 201) {
        logger.info("Reviewers assigned successfully.");
      }
      else {
        logger.error("Error assigning reviewers. Status code: {}", statusCode);
      }
    }
    return null;
  }

  private String invokeGetPullRequestEndPoint() {
    GetPullRequestEndPoint getPullRequestEndPoint = new GetPullRequestEndPoint("com.aexp.acq.go2.rest_interactions.GetPullRequestEndPoint");
    RestResponse response = (RestResponse)getPullRequestEndPoint.execute(App.instance().getProperty("github.base.url"), App.instance().getProperty("github.repo"), App.instance().getProperty("reviewers.pull.request.number"));
    if (response != null && response.getStatus() == 200) {
      Document pullRequestDoc = new JDocument(response.getResponseBody());
      if (pullRequestDoc.pathExists("$.base.ref")) {
        return pullRequestDoc.getString("$.base.ref");
      }
    }
    return null;
  }

  private int invokeAssignReviewersEndPoint(String payload) {
    AssignReviewersEndPoint assignReviewersEndPoint = new AssignReviewersEndPoint("com.aexp.acq.go2.rest_interactions.AssignReviewersEndPoint");
    RestResponse response = (RestResponse)assignReviewersEndPoint.execute(App.instance().getProperty("github.base.url"), App.instance().getProperty("github.repo"), App.instance().getProperty("reviewers.pull.request.number"), payload);
    return response.getStatus();
  }

}
