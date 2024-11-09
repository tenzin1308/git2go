package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.utils.ConflictHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SyncBranch extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(SyncBranch.class);
  private final ConflictHandler conflictHandler;
  private final String refBranch = App.instance().getProperty("ref.branch");
  private final String baseBranch = App.instance().getProperty("base.branch");
  private final String conflictResolution = App.instance().getProperty("conflict.resolution");
  private final boolean dryRun = Boolean.getBoolean(App.instance().getProperty("dry.run"));

  public SyncBranch(String name) {
    super(name);
    this.conflictHandler = new ConflictHandler(conflictResolution);
  }

  @Override
  protected Object process(Object... vargs) {
    String pullNumber = null;
    try {
      // logic to create a new branch from the base branch
      String syncBranch;
      // merge the ref branch into the sync branch

      if (dryRun) {
        logger.info("Dry run enabled. No changes will be made to the branch");
      }
      else {
        // logic to generate a pull request for the sync branch to the base branch
      }

    }
    catch (RuntimeException e) {
      logger.error("Error while syncing branch -> {}", e.getMessage());
    }
    return pullNumber;
  }

  private String createSyncBranch(String baseBranch) throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date date = new Date();
    String today = dateFormat.format(date);
    String newBranch = baseBranch + "-" + today + "-sync";

    // logic to create a new branch from the base branch
    return newBranch;
  }

}
