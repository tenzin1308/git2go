package com.aexp.acq.go2.github_actions.maven_cli;

import com.aexp.acq.go2.config.DefaultKey;
import jakarta.validation.constraints.NotBlank;

public final class MavenCLIConfig {

  @NotBlank(message = "command is required")
  public String command;

  @DefaultKey("working_dir")
  public String workingDir;

}
