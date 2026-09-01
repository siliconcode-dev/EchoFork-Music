# Changelog

All notable changes to Enhanced Echo Music are documented here.

## [0.1.1] — 2026-09-01

### Added
- First release of Enhanced Echo Music, a fork of [Echo Music](https://github.com/iad1tya/Echo-Music) with its own violet identity.
- Spatial Audio (Beta) — built on Android's `Spatializer` API (Android 13+).
- Immersive Audio Passthrough (Beta) — detects a device's own Dolby audio engine and deep-links straight to the system Sound settings to manage it (Android doesn't allow apps to control it directly).
- True Motion (Beta) — adaptive high refresh rate support, matching the device's detected display modes, with manual override.
- Wavy progress indicators on the player screens (Material 3 Expressive), in the app's brand violet.
- A "What's New" dialog summarizing new features on first launch after an update.

### Changed
- Versioning reset to `0.1.1` to reflect this fork's actual history, rather than inheriting upstream's `1.2.1`.
- Removed iOS target scaffolding across all shared modules — this fork ships Android only. JVM/desktop scaffolding remains untouched for a possible future.
- Crashlytics and Analytics now run against this fork's own Firebase project by default.

### Removed
- iOS build targets and source sets (`iosArm64`, `iosSimulatorArm64`) from every shared module.
- The legacy, unused `app/` module inherited from before this fork's restructure.
