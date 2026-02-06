package com.aexp.acq.go2.github_actions.publish_artifact;

import com.aexp.acq.go2.config.DefaultKey;
import jakarta.validation.constraints.NotBlank;

public final class PublishArtifactConfig {

  @NotBlank(message = "image name is required")
  public String imageName;

  @NotBlank(message = "image tag is required")
  public String imageTag;

  @DefaultKey("artifactory_registry")
  public String artifactoryRegistry;

}
