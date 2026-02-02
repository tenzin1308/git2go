package com.aexp.acq.go2.utils.logging;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

@Plugin(
        name = "GitHubAnnotationAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE
)
public class GitHubAnnotationAppender extends AbstractAppender {

  protected GitHubAnnotationAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout, true);
  }

  @PluginFactory
  public static GitHubAnnotationAppender createAppender(@PluginAttribute("name") String name) {
    return new GitHubAnnotationAppender(name, null, PatternLayout.createDefaultLayout());
  }

  @Override
  public void append(LogEvent event) {
    String prefix = switch (event.getLevel().getStandardLevel()) {
      case ERROR -> "::error::";
      case WARN -> "::warning::";
      default -> "";
    };
    System.out.println(prefix + event.getMessage().getFormattedMessage());
  }

}
