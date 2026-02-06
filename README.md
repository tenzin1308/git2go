# Git2Go GitHub Action

This repository implements a **single GitHub Action** that can run **multiple sub-actions** (plugins) using one
entrypoint.
Users select **which sub-action to run** via `action_name` and pass a **typed YAML configuration** via `config`.
The action runs as a **Docker action**, executing a **Java fat (uber) JAR**.

---

## How it works (end-to-end)

1. GitHub Actions launches the Docker container
2. GitHub injects:<br>- `with:` inputs as `INPUT_*` env vars
   <br>- all `GITHUB_*` env vars
   <br>- any secrets / env vars defined in the workflow
3. The Java entrypoint (`Git2Go`) runs:<br>- loads platform config into `App`
   <br>- selects the requested sub-action
   <br>- parses and validates the action config
   <br>- executes the action

---

## Inputs

This action has **exactly two inputs**.

```yaml
inputs:
  action_name:
    required: true
  config:
    required: true
```

| **Input**   | **Required** | **Description**           |
|-------------|--------------|---------------------------|
| action_name | yes          | Which sub-action to run   |
| config      | yes          | Inline YAML configuration |

---

## Basic Usage

```yaml
- uses: amex-eng/git2go@v1
  with:
    action_name: publish-artifact
    config: |
      artifactory_url: 
      artifactory_path:
      path:
  env:
    BUILD_PROFILE: prod
```

---

## Configuration model

There are **two kinds of configuration**.

---

### 1. Platform configuration (global)

Platform configuration is merged into a global singleton called App.

#### Precedence (lowest → highest)

Environment variables <br>
↓<br>
Config.properties<br>
↓<br>
Platform: block in YAML<br>
↓<br>
action config fields<br>

After merging, values are accessed in Java as:

```java
String githubUrl = App.instance().getProperty("github.server.url");
```

---

#### Environment variable normalization

All environment variable are imported automatically.

#### Rule:

ENV_NAME → eng.name

#### Examples:

| **Env var**       | **App key**       |
|-------------------|-------------------|
| GITHUB_SERVER_URL | github.server.url |
| GITHUB_REPOSITORY | github.repository |
| GITHUB_WORKSPACE  | github.workspace  |
| ARTIFACTORY_TOKEN | artifactory.token |
| FEATURE_X_ENABLED | feature.x.enabled |

---

### Passing secret environment variables

```yaml
- uses: amex-eng/git2go@v1
  with:
    action_name: publish-artifact
    config: |
      artifactory_url: 
      artifactory_path:
      path:
  env:
    ARTIFACTORY_USER: ${{ secrets.ARTIFACTORY_USER }}
    ARTIFACTORY_PASS: ${{ secrets.ARTIFACTORY_PASS }}
```

```java
String artifactoryUser = App.instance().getProperty("artifactory.user");
```

### Passing non-secret environment variables

```yaml
- uses: amex-eng/git2go@v1
  with:
    action_name: publish-artifact
    config: |
      artifactory_url: 
      artifactory_path:
      path:
  env:
    BUILD_PROFILE: prod
```

```java
String profile = App.instance().getProperty("build.profile");
```

### Using the platform block (global overrides)

This option `platform:` block allows users to override **platform-level-defaults** for a single run.

```yaml
- uses: amex-eng/git2go@v1
  with:
    action_name: publish-artifact
    config: |
      platform:
        github_server_url: <CHANGE THE URL>
      artifactory_url: 
      artifactory_path:
      path:
  env:
    BUILD_PROFILE: prod
```

---

## Using outputs from previous steps

GitHub resolves `${{ ... }}` before the container runs.

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - id: version
        run: |
          echo "verison=1.2.3" >> $GITHUB_OUTPUT
      - uses: amex-eng/git2go@v1
        with:
          action_name: maven-cli
          config: |
            command: "mvn -Dapp.version=${{ steps.version.outputs.version }} test"
```

---

## Adding a new sub-action

### Config class

```java
public final class MyActionConfig {

  @NotBlank
  public String foo;

}
```

### Action class

```java
public final class MyAction implements Action<MyActionConfig> {

  public String name() {
    return "my-action";
  }

  public Class<MyActionConfig> configClass() {
    return MyACtionConfig.class;
  }

  public void execute(MyActionConfig cfg) {
    // implementation
  }

}
```

### Register action

```bash
  src/main/resources/META-INF/services/com.aexp.acq.go2.core.Action
```

```text
com.aexp.acq.go2.github_actions.MyAction
```

