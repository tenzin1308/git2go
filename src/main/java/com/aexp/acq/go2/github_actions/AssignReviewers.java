package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.*;
import com.aexp.acq.go2.utils.BaseUtils;
import com.aexp.acq.go2.utils.Status;
import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Status(value = "TESTING_IN_PROGRESS",
        description = "This component uses the GitHub Rest API to assign reviewers to a pull request")
public class AssignReviewers extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(AssignReviewers.class);
  private final Document configDoc = BaseUtils.getResouceAsDocument(AssignReviewers.class, App.instance().getProperty("reviewers.config.path"), "assign_reviewers_config");

  protected AssignReviewers(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    // if config doc is null, exit with error
    if (configDoc == null) {
      logger.error("Config document not found. Exiting.");
      System.exit(1);
    }
    int pullNumber = Integer.parseInt(App.instance().getProperty("reviewers.pull.request.number"));
    // first get the pull request details -> the base branch
    String baseBranch = invokeGetPullRequestEndPoint();
    // get the requested reviewers from the config doc based on the base branch
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
    RestInteraction restInteraction = (RestInteraction)Git2GoComponentFactory.instance().getComponent("com.aexp.acq.go2.rest_interactions.GetPullRequestEndPoint");
    RestResponse response = (RestResponse)restInteraction.execute(App.instance().getProperty("github.base.url"), App.instance().getProperty("github.repo"), App.instance().getProperty("reviewers.pull.request.number"));
    if (response != null && response.getStatus() == 200) {
      Document pullRequestDoc = new JDocument(response.getResponseBody());
      if (pullRequestDoc.pathExists("$.base.ref")) {
        return pullRequestDoc.getString("$.base.ref");
      }
    }
    return null;
  }

  private int invokeAssignReviewersEndPoint(String payload) {
    RestInteraction restInteraction = (RestInteraction)Git2GoComponentFactory.instance().getComponent("com.aexp.acq.go2.rest_interactions.AssignReviewersEndPoint");
    RestResponse response = (RestResponse)restInteraction.execute(App.instance().getProperty("github.base.url"), App.instance().getProperty("github.repo"), App.instance().getProperty("reviewers.pull.request.number"), payload);
    return response.getStatus();
  }

}
