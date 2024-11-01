package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
import com.aexp.acq.go2.rest_interactions.ListPullRequestEndPoint;
import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PurgeStalePRs extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(PurgeStalePRs.class);
  String url = App.instance().getProperty("github.base.url");
  String gitRepo = App.instance().getProperty("github.repo");
  Document pullRequests = new JDocument();
  Document actionResponse = new JDocument("stale_actions_response", null);


  public PurgeStalePRs(String name) {
    super(name);
  }

  @Override
  protected Object process(Object... vargs) {
    listPRs();
    for (int i = 0; i < pullRequests.getArraySize("$.list_pull_requests[]"); i++) {
      try {
        String updatedAt = pullRequests.getString("$.list_pull_requests[%].updated_at", String.valueOf(i));
        if (BaseUtils.isNotNullOrEmpty(updatedAt) && BaseUtils.isStale(updatedAt, App.instance().getProperty("days.before.stale.pr"), "uuuu-MM-dd'T'HH:mm:ssX")) {
          List<String> reviewersList = new ArrayList<>();
          try {
            for (int j = 0; j < pullRequests.getArraySize("$.list_pull_requests[%].requested_reviewers[]", String.valueOf(i)); j++) {
              reviewersList.add(pullRequests.getString("$.list_pull_requests[%].requested_reviewers[%].login", String.valueOf(i), String.valueOf(j)));
            }
          }
          catch (Exception e) {
            logger.error("Error while fetching reviewers for pull number {} -> {}",pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
          }

        }
      }
    }
  }

  private void listPRs() {
    int page = 1;
    while (true) {
      String currentPage = String.valueOf(page++);
      String response = invokeListPullRequestEndPoint(url, gitRepo, currentPage);
      Document responseDoc = new JDocument(response);
      if (responseDoc.getArraySize("$.[]") == 0) {
        break;
      }
      for (int i = 0; i < responseDoc.getArraySize("$.[]"); i++) {
        Document pr = responseDoc.getDocument("$.[%]", String.valueOf(i));
        pullRequests.setContent(pr, "$", "$.list_pull_requests[%]", String.valueOf(pullRequests.getArraySize("$.list_pull_requests[]")));
      }
    }
  }

  private String invokeListPullRequestEndPoint(String url, String gitRepo, String page) {
    ListPullRequestEndPoint listPullRequestEndPoint = new ListPullRequestEndPoint("ListPullRequestEndPoint");
    RestResponse restResponse = (RestResponse)listPullRequestEndPoint.execute(url, gitRepo, page);
    return restResponse.getResponseBody();
  }

}
