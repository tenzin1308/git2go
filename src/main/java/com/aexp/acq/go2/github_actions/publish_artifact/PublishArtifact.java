package com.aexp.acq.go2.github_actions.publish_artifact;

import com.aexp.acq.go2.core.Action;

public final class PublishArtifact implements Action<PublishArtifactConfig> {

  @Override
  public String name() {
    return "Publish Artifact";
  }

  @Override
  public Class<PublishArtifactConfig> configClass() {
    return PublishArtifactConfig.class;
  }

  @Override
  public void execute(PublishArtifactConfig cfg) throws Exception {
    //    String githubOutputPath = System.getenv("GITHUB_OUTPUT");
    //    if (githubOutputPath == null) {
    //      throw new IllegalArgumentException("GITHUB_OUTPUT is not set (not running in GitHub Actions)");
    //    }
    //
    //    ProcessBuilder pb = new ProcessBuilder(
    //            "curl",
    //            "-v", "-u", App.instance().getProperty("artifactory.username") + ":" + App.instance().getProperty("artifactory.password"),
    //            "--upload-file",
    //            App.instance().getProperty("artifactory.image"),
    //            App.instance().getProperty("artifactory.base_url") + App.instance().getProperty("artifactory.url")
    //    );
    //
    //    pb.redirectErrorStream(true);
    //
    //    Process process = pb.start();
    //
    //    Path logFile = Paths.get("upload.log");
    //
    //    StringBuilder output = new StringBuilder();
    //
    //    try (
    //      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    //      BufferedWriter writer = Files.newBufferedWriter(logFile,
    //              StandardOpenOption.CREATE,
    //              StandardOpenOption.TRUNCATE_EXISTING)
    //    ) {
    //      String line;
    //      while ((line = reader.readLine()) != null){
    //        // echo to console
    //        System.out.println(line);
    //
    //        // tee to file
    //        writer.write(line);
    //        writer.newLine();
    //
    //        // collect for GITHUB_OUTPUT
    //        output.append(line).append("\n");
    //      }
    //    }
    //
    //    int exitCode = process.waitFor();
    //
    //    // ---- Write multi-line output to GITHUB_OUTPUT ----
    //    w
  }

}
