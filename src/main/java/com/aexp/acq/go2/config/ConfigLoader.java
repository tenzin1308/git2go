package com.aexp.acq.go2.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.nio.file.Path;

public class ConfigLoader {

  public static ActionConfig load() throws Exception {
    String path = System.getenv("GITHUB_WORKSPACE") + "/action.yml";

    Yaml yaml = new Yaml();
    try (FileInputStream in = new FileInputStream(Path.of(path).toFile())) {
      return yaml.loadAs(in, ActionConfig.class);
    }
  }

}
