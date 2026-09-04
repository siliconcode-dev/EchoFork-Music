# Release Info — Enhanced Echo Music

The full, up-to-date release history lives in [`CHANGELOG.md`](CHANGELOG.md)
— check there for what shipped in which version. This file covers the
release process itself, not the history.

## Release process

1. Version bump: `gradle/libs.versions.toml`'s `version-name` and
   `version-code`.
2. `CHANGELOG.md`: a new `## [x.y.z] — YYYY-MM-DD` entry, grouped under
   `### Added` / `### Fixed` / `### Changed` as applicable.
3. Commit, then an annotated git tag matching the version (`vX.Y.Z`).
4. Pushing the tag triggers CI to build and publish a GitHub Release with
   4 APK variants (universal, arm64-v8a, armeabi-v7a, x86_64).

None of steps 3-4 happen without explicit instruction — see `AGENT.md`'s
"Do not push without explicit instruction" rule.

## Pull Request & Release Note Guidelines

Contributions to `CHANGELOG.md` should follow Conventional Commits style
for the underlying commit/PR title (`<type>(<scope>): <summary>`), and
changelog entries should describe user-visible behavior — what changed
for someone using the app — not internal implementation detail.
