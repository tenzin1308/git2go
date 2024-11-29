# Git2Go

Welcome to the Git2Go, a collection of reusable actions designed to streamline your development workflow. This
repository houses multiple GitHub Actions under one umbrella, making it easy to manage and implement essential tasks for
your projects.

## Available Actions

### 1. Checkstyle Action

The Checkstyle Action runs static code analysis on Java files in your repository
using [Checkstyle](https://checkstyle.sourceforge.io/). It checks for coding standard violations and comments inline on
the lines where violations occur.

**Inputs:**

- `config_file` (optional): URL or path to a custom Checkstyle configuration file (default: Google Checkstyle).
- `commit_sha`: The commit SHA to analyze (inferred from the PR).

[//]: # (Link to another readme file for checkstyle action)

[Checkstyle Action](docs/checkstyle/README.md)

### 2. Maven CLI Action

The Maven CLI Action allows you to run Maven commands directly from your workflow. It supports various Maven goals.

**Inputs:**

- `maven_command`: The Maven command to execute (e.g., `clean install`).

### 3. Purge Stale Branch Action

This action identifies and deletes stale branches in your repository that have not had recent activity.

**Inputs:**

- `days`: The number of days of inactivity to consider a branch stale (default: 30).

### 4. Purge Stale PRs Action

This action automatically closes pull requests that have not seen any activity for a specified period.

**Inputs:**

- `days`: The number of days of inactivity to consider a PR stale (default: 30).

## Upcoming Actions

### 5. Sync Branch Action

This action will synchronize a specified branch with its upstream counterpart, ensuring that it is up to date with the
latest changes.

### 6. Assign Reviewers Action

Automatically assigns reviewers to pull requests based on predefined criteria.

### 7. Merge Check Action

Checks various conditions (such as build success and code style compliance) before allowing a pull request to be merged.

### 8. Deployments Action

Handles deployments for your applications, ensuring that the necessary conditions are met before deployment is executed.

## Usage

To use any of these actions in your workflow, reference the action in your workflow YAML file like so:

```yaml
jobs:
  example_job:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v2
      
      - name: Run Checkstyle
        uses: amex-eng/git2go@v1.0.0 # Refer to the desired version
        with:
          action_name: 'checkstyle'
          config_file: 'https://example.com/checkstyle.xml'
          commit_sha: ${{ github.sha }}
