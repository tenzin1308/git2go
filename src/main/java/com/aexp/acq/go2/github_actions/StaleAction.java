package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
import com.aexp.acq.go2.rest_interactions.DeleteReferenceEndPoint;
import com.aexp.acq.go2.rest_interactions.GetBranchEndPoint;
import com.aexp.acq.go2.rest_interactions.ListBranchesEndPoint;
import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaleAction extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(StaleAction.class);
  String url = App.instance().getProperty("github.base.url");
  String gitRepo = App.instance().getProperty("github.repo");
  Document branches = new JDocument();
  Document actionResponse = new JDocument("stale_actions_response", null);

  public StaleAction(String name) {
    super(name);
  }

  @Override
  protected Object process(Object... vargs) {
    listBranches();
    for (int i = 0; i < branches.getArraySize("$.list_branches[]"); i++) {
      try {
        String branchName = branches.getString("$.list_branches[%].name", String.valueOf(i));
        if (BaseUtils.isBranchExcluded(branchName)) {
          logger.info("Branch is excluded -> {}", branchName);
          continue;
        }
        String response = invokeGetBranchEndPoint(url, gitRepo, branchName);
        Document responseDoc = new JDocument(response);
        String updatedAt = responseDoc.getString("$.commit.commit.committer.date"); // "uuuu-MM-dd'T'HH:mm:ss'X'"
        if (BaseUtils.isStale(updatedAt, App.instance().getProperty("days.before.stale.branch"), "uuuu-MM-dd'T'HH:mm:ssX")) {
          logger.info("Stale Branch -> {}, last updated at -> {}", branchName, updatedAt);
          actionResponse.setString("$.stale_actions_response.stale_branch[branch_name=%].branch_name", branchName, branchName);
          actionResponse.setString("$.stale_actions_response.stale_branch[branch_name=%].branch_updated_at", updatedAt, branchName);
          RestResponse restResponse = invokeDeleteReferenceEndPoint(url, gitRepo, branchName);
          if (restResponse.getStatus() == 204) {
            logger.info("Branch deleted successfully -> {}", branchName);
            actionResponse.setBoolean("$.stale_actions_response.stale_branch[branch_name=%].is_purge", true, branchName);
          }
          else {
            logger.error("Error while deleting branch -> {}", branchName);
            actionResponse.setBoolean("$.stale_actions_response.stale_branch[branch_name=%].is_purge", false, branchName);
            actionResponse.setString("$.stale_actions_response.stale_branch[branch_name=%].error_message", restResponse.getMessage(), branchName);
          }
        }
      }
      catch (Exception e) {
        logger.error("Error while checking if branch is excluded -> {}", e.getMessage());
      }

    }
    return actionResponse.getPrettyPrintJson();
  }

  private Document listBranches() {
    int page = 1;
    while (true) {
      String currentPage = String.valueOf(page++);
      String response = invokeListBranchesEndPoint(url, gitRepo, currentPage);
      Document responseDoc = new JDocument(response);
      logger.info("Response Document -> {}", responseDoc.getJson());
      if (responseDoc.getArraySize("$.[]") == 0) {
        break;
      }
      for (int i = 0; i < responseDoc.getArraySize("$.[]"); i++) {
        Document branch = responseDoc.getDocument("$.[%]", String.valueOf(i));
        branches.setContent(branch, "$", "$.list_branches[%]", String.valueOf(branches.getArraySize("$.list_branches[]")));
      }
    }

    return branches;
  }

  private String invokeListBranchesEndPoint(String url, String gitRepo, String currentPage) {
    ListBranchesEndPoint listBranchesEndPoint = new ListBranchesEndPoint("com.aexp.acq.go2.rest_interactions.ListBranchesEndPoint");
    RestResponse restResponse = (RestResponse)listBranchesEndPoint.execute(url, gitRepo, currentPage);
    return restResponse.getResponseBody();
  }

  private String invokeGetBranchEndPoint(String url, String gitRepo, String branch) {
    GetBranchEndPoint getBranchEndPoint = new GetBranchEndPoint("com.aexp.acq.go2.rest_interactions.GetBranchEndPoint");
    RestResponse restResponse = (RestResponse)getBranchEndPoint.execute(url, gitRepo, branch);
    return restResponse.getResponseBody();
  }

  private RestResponse invokeDeleteReferenceEndPoint(String url, String gitRepo, String branch) {
    DeleteReferenceEndPoint deleteReferenceEndPoint = new DeleteReferenceEndPoint("com.aexp.acq.go2.rest_interactions.DeleteReferenceEndPoint");
    return (RestResponse)deleteReferenceEndPoint.execute(url, gitRepo, branch);
  }

}
