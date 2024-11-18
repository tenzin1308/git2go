package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    String pullRequestId = fetchPullRequestId(owner, repo, App.instance().getProperty("reviewers.pull.request.number"));

    return null;
  }

  private String fetchPullRequestId(String owner, String repo, String pullRequestNumber) {
    String query = """
            query {
              repository(owner: "%s", name: "%s") {
                pullRequest(number: %s) {
                  id
                }
              }
            }
            
            """.formatted(owner, repo, pullRequestNumber);
    return query;
  }


}
