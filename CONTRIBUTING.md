# Contributing to Enhanced Echo Music

We welcome contributions from the community. To ensure a smooth collaboration process, please adhere to the following guidelines.

## Code of Conduct

Maintain a professional, respectful, and inclusive environment in all interactions within issues, pull requests, and discussions. See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for the full policy.

## Development Workflow

1. **Fork the Repository:** Create a personal fork of the project on GitHub.
2. **Clone the Repository:** Clone your fork locally. A plain `git clone` is sufficient — the `core` modules are vendored in this repository, not tracked as a submodule.
3. **Create a Branch:** Create a dedicated branch for your feature or bug fix (`git checkout -b feature/your-feature-name` or `bugfix/issue-description`).
4. **Implement Changes:** Write clean, documented, and testable code adhering to the existing project architecture.
5. **Commit Standards:** Use clear and descriptive commit messages.
6. **Submit a Pull Request:** Open a pull request against the `main` branch. Provide a comprehensive description of the changes, the rationale, and any relevant issue numbers.

## Building and Checks

Requires **JDK 21** and **Android SDK 37**.

```bash
./gradlew assembleDebug   # build
./gradlew lint            # Android lint
```

Please make sure `assembleDebug` succeeds before opening a pull request.

## Issue Reporting

Before submitting a new issue or feature request, please search the existing issues to prevent duplicates. When creating an issue, utilize the provided templates and supply as much technical detail as possible, including logs, device specifications, and steps to reproduce.

Found a security vulnerability instead? Don't open a public issue — see [SECURITY.md](SECURITY.md) for how to report it privately.
