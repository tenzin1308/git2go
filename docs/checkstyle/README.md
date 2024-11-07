# Check Style

This action runs the Checkstyle tool on your Java codebase to ensure that it adheres to the defined coding standards.

## Inputs

| Input                            | Description                                                 | Default           |
|----------------------------------|-------------------------------------------------------------|-------------------|
| `checkstyle.pull.request.number` | **Required** The pull request number to run the check       | None              |
| `checkstyle.config.path`         | **Optional** Path to a custom Checkstyle configuration file | Google Checkstyle |

## Example usage

```yaml
uses: actions/checkstyle@v1
env:
  checkstyle.pull.request.number: 1
  checkstyle.config.path: 'checkstyle.xml'
```
