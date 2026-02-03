package com.aexp.acq.go2;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.config.ActionConfig;
import com.aexp.acq.go2.config.ConfigLoader;
import com.aexp.acq.go2.config.ConfigValidator;
import com.aexp.acq.go2.core.Action;
import com.aexp.acq.go2.core.ActionContext;
import com.aexp.acq.go2.core.ActionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Git2Go {

  private static final Logger log = LoggerFactory.getLogger(Git2Go.class);

  public static void main(String[] args) {
    try {
      log.info("::group::Git2Go Action");

      // App Initialization
      {
        App.init(Git2Go.class.getSimpleName());
        App.instance().loadProperties("config.properties");
        App.instance().loadEnvironmentProperties();
        App.loadDocumentModels("doc_models", "models/");
      }

      // Load action config
      ActionConfig config = ConfigLoader.load();
      ConfigValidator.validate(config);

      // Execute action
      Action action = ActionRegistry.get(config.getAction());
      log.info("::group::Running actions: {}", action.name());
      action.execute(new ActionContext(config));
      log.info("::endgroup::");

      log.info("::endgroup::");
    }
    catch (Exception e) {
      log.error("Action failed: {}", e.getMessage(), e);
      System.exit(1);
    }
  }

}
