package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
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

  public StaleAction(String name) {
    super(name);
  }

  @Override
  protected Object process(Object... vargs) {
    listBranches();
    for (int i = 0; i < branches.getArraySize("$.list_branches[]"); i++) {
      try {
        if (BaseUtils.isBranchExcluded(branches.getString("$.list_branches[%].name", String.valueOf(i)))) {
          logger.info("Branch is excluded -> {}", branches.getString("$.list_branches[%].name", String.valueOf(i)));
          continue;
        }
        String response = invokeGetBranchEndPoint(url, gitRepo, branches.getString("$.list_branches[%].name", String.valueOf(i)));
        Document responseDoc = new JDocument(response);
        String updatedAt = responseDoc.getString("$.commit.commit.committer.date"); // "uuuu-MM-dd'T'HH:mm:ss'X'"
        if (BaseUtils.isStale(updatedAt, App.instance().getProperty("days.before.stale.branch"), "uuuu-MM-dd'T'HH:mm:ssX")) {
          logger.info("Branch is stale -> {}", branches.getString("$.list_branches[%].name", String.valueOf(i)));

        }
      }
      catch (Exception e) {
        logger.error("Error while checking if branch is excluded -> {}", e.getMessage());
      }

    }
    return null;
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

}
