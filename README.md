# Git2Go GitHub Action

This repository implements a **single GitHub Action** that can run **multiple sub-actions** (plugins) using one
entrypoint.
Users select **which sub-action to run** via `action_name` and pass a **typed YAML configuration** via `config`.
The action runs as a **Docker action**, executing a **Java fat (uber) JAR**.

---

## Table of Contents

- [How it works (end-to-end)](#how-it-works-end-to-end)
- [Inputs](#inputs)
- [Basic Usage](#basic-usage)
- [Available & Upcoming Actions](#available--upcoming-actions)
- [Configuration model](#configuration-model)
    - [Platform configuration (global)](#1-platform-configuration-global)
    - [Environment variable normalization](#environment-variable-normalization)
    - [Passing secret environment variables](#passing-secret-environment-variables)
    - [Passing non-secret environment variables](#passing-non-secret-environment-variables)
    - [Using the platform block (global overrides)](#using-the-platform-block-global-overrides)
- [Using outputs from previous steps](#using-outputs-from-previous-steps)
- [Adding a new sub-action](#adding-a-new-sub-action)

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
    action_name: artifact-publisher
    config: |
      artifactory_url: 
      artifactory_path:
      path:
  env:
    BUILD_PROFILE: prod
```

---

## Available & Upcoming Actions

Use the value in the **Action Name** column as the `action_name` input in your workflow.
Actions marked as **Coming Soon** are planned and not yet available in the current release.

| **Action Name**      | **Description**                                       | **Docs**           |
|----------------------|-------------------------------------------------------|--------------------|
| `github-artifact`    | Upload and download GitHub Actions artifacts          | _Coming Soon_      |
| `github-release`     | Create Git tags and GitHub releases                   | _Coming Soon_      |
| `artifact-publisher` | Publish build artifacts to Artifactory                | _Coming Soon_      |
| `docker-publisher`   | Build and publish Docker images to a registry         | _Coming Soon_      |
| `close-stale`        | Automatically close stale PRs and Branches            | _Coming Soon_      |
| `maven-cli`          | Run arbitrary Maven commands                          | [View](#maven-cli) |
| `assign-reviewers`   | Automatically assign reviewers to pull requests       | _Coming Soon_      |
| `sync-branch`        | Sync a branch with its upstream or parent branch      | _Coming Soon_      |
| `run-checkstyle`     | Run Checkstyle and report violations on Java projects | _Coming Soon_      |

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

ENV_NAME → env.name

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
    action_name: artifact-publisher
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
    action_name: artifact-publisher
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
    action_name: artifact-publisher
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
          echo "version=1.2.3" >> $GITHUB_OUTPUT
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

