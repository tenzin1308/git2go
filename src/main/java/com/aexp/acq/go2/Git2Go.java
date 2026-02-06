package com.aexp.acq.go2;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.config.ConfigLoader;
import com.aexp.acq.go2.config.Defaults;
import com.aexp.acq.go2.config.ValidationUtil;
import com.aexp.acq.go2.core.Action;
import com.aexp.acq.go2.core.ActionRegistry;
import com.aexp.acq.go2.utils.logging.ActionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Git2Go {

  private static final Logger log = LoggerFactory.getLogger(Git2Go.class);

  public static void main(String[] args) {
    try {
      ActionLogger.startGroup(log, "Git2Go Action");

      //
      String actionName = System.getenv("INPUT_ACTION_NAME");
      String configYaml = System.getenv("INPUT_CONFIG");

      // App Initialization
      {
        ActionLogger.startGroup(log, "App Initialization");
        App.init(Git2Go.class.getSimpleName());
        App.instance().loadEnvironmentProperties();
        App.instance().loadProperties("config.properties");
        Map<String, Object> platform = ConfigLoader.extractPlatformOverrides(configYaml);
        App.instance().applyPlatformOverrides(platform);
        ActionLogger.endGroup(log);
      }

      App.loadDocumentModels("doc_models", "models/");
      String actionYaml = ConfigLoader.removePlatformBlock(configYaml);
      Action<?> action = ActionRegistry.get(actionName);

      ActionLogger.startGroup(log, "Parse + Validate");
      Object cfg = ConfigLoader.parseStrict(actionYaml, action.configClass());
      cfg = Defaults.apply(cfg);
      ValidationUtil.validateOrThrow(cfg);
      validateUnchecked(action, cfg);
      ActionLogger.endGroup(log);

      // Execute action
      ActionLogger.startGroup(log, "Run: " + action.name());
      executeUnchecked(action, cfg);
      ActionLogger.endGroup(log);

      ActionLogger.endGroup(log);
    }
    catch (Exception e) {
      log.error("Action failed: {}", e.getMessage(), e);
      System.exit(1);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void executeUnchecked(Action action, Object cfg) throws Exception {
    action.execute(cfg);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void validateUnchecked(Action action, Object cfg) {
    action.validate(cfg);
  }

}
