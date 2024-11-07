package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
import com.aexp.acq.go2.rest_interactions.AddLabelsEndPoint;
import com.aexp.acq.go2.rest_interactions.CreateIssueCommentEndPoint;
import com.aexp.acq.go2.rest_interactions.ListPullRequestEndPoint;
import com.aexp.acq.go2.rest_interactions.UpdatePullRequestEndPoint;
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
            logger.error("Error while fetching reviewers for pull number {} -> {}", pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
          }
          String customComment = getCustomComment(pullRequests.getString("$.list_pull_requests[%].user.login", String.valueOf(i)), reviewersList);
          boolean purgeFlag = false;
          if (pullRequests.getArraySize("$.list_pull_requests[%].labels[]", String.valueOf(i)) > 0) {
            for (int k = 0; k < pullRequests.getArraySize("$.list_pull_requests[%].labels[]", String.valueOf(i)); k++) {
              if (App.instance().getProperty("stale.pr.labels").equalsIgnoreCase(pullRequests.getString("$.list_pull_requests[%].labels[%].name", String.valueOf(i), String.valueOf(k)))) {
                purgeFlag = true;
                break;
              }
            }
          }
          if (purgeFlag == true) {
            try {
              invokeUpdatePullRequestEndPoint(url, gitRepo, String.valueOf(pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i))), "{\"state\":\"closed\"}");
            }
            catch (Exception e) {
              logger.error("Error while closing pull number {} -> {}", pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
              // TODO: Add to actionResponse
            }
          }
          else {
            try {
              invokeCreateIssueCommentEndPoint(url, gitRepo, String.valueOf(pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i))), customComment);
            }
            catch (Exception e) {
              logger.error("Error while commenting on pull number {} -> {}", pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
              // TODO: Add to actionResponse
            }
            try {
              invokeAddLabelsEndPoint(url, gitRepo, String.valueOf(pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i))), "{\"labels\":[\"stale\"]}");
            }
            catch (Exception e) {
              logger.error("Error while adding label to pull number {} -> {}", pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
              // TODO: Add to actionResponse
            }
          }
        }
      }
      catch (Exception e) {
        logger.error("Error while processing pull number {} -> {}", pullRequests.getInteger("$.list_pull_requests[%].number", String.valueOf(i)), e.getMessage());
        // TODO: Add to actionResponse
      }
    }
    return null;
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
    ListPullRequestEndPoint listPullRequestEndPoint = new ListPullRequestEndPoint("com.aexp.acq.go2.rest_interactions.ListPullRequestEndPoint");
    RestResponse restResponse = (RestResponse)listPullRequestEndPoint.execute(url, gitRepo, page);
    return restResponse.getResponseBody();
  }

  private String invokeUpdatePullRequestEndPoint(String url, String gitRepo, String prNumber, String payload) {
    UpdatePullRequestEndPoint updatePullRequestEndPoint = new UpdatePullRequestEndPoint("com.aexp.acq.go2.rest_interactions.UpdatePullRequestEndPoint");
    RestResponse restResponse = (RestResponse)updatePullRequestEndPoint.execute(url, gitRepo, prNumber, payload);
    return restResponse.getResponseBody();
  }

  private String invokeCreateIssueCommentEndPoint(String url, String gitRepo, String prNumber, String payload) {
    CreateIssueCommentEndPoint createIssueCommentEndPoint = new CreateIssueCommentEndPoint("com.aexp.acq.go2.rest_interactions.CreateIssueCommentEndPoint");
    RestResponse restResponse = (RestResponse)createIssueCommentEndPoint.execute(url, gitRepo, prNumber, payload);
    return restResponse.getResponseBody();
  }

  private String invokeAddLabelsEndPoint(String url, String gitRepo, String prNumber, String payload) {
    AddLabelsEndPoint addLabelsEndPoint = new AddLabelsEndPoint("com.aexp.acq.go2.rest_interactions.AddLabelsEndPoint");
    RestResponse restResponse = (RestResponse)addLabelsEndPoint.execute(url, gitRepo, prNumber, payload);
    return restResponse.getResponseBody();
  }

  private String getCustomComment(String author, List<String> reviewersList) {
    if (reviewersList.size() > 0) {
      String reviewers = "@" + (String.join(", @", reviewersList));
      return String.format("@%s, %s This PR hasn't seen activity in last %s days!\n" +
                      "Should it be **merged, closed, or worked** on further?\n" +
                      "If you want to keep it open, **post a comment or remove** the `stale` label - otherwise this will be closed in next %s days.",
              author, reviewers, App.instance().getProperty("days.before.stale.pr"), App.instance().getProperty("days.before.close.stale.pr"));
    }
    return String.format("@%s This PR hasn't seen activity in last %s days!\n" +
                    "Should it be **merged, closed, or worked** on further?\n" +
                    "If you want to keep it open, **post a comment or remove** the `stale` label - otherwise this will be closed in next %s days.",
            author, App.instance().getProperty("days.before.stale.pr"), App.instance().getProperty("days.before.close.stale.pr"));
  }

}
