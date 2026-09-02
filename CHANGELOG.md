# Changelog

All notable changes to Enhanced Echo Music are documented here.

## [0.1.2] — 2026-09-02

### Fixed
- Adding a song to a playlist now inserts it at the top instead of always appending it to the bottom (synced YouTube playlists are kept in sync too).
- Playlist reordering: "Change order" no longer requires switching to Custom Order sort first; fixed a bug where dragging a song a long distance in an unsynced playlist could corrupt the list order; added "Move to top" / "Move to bottom" to each song's menu as a precise, non-drag option.
- Endless-queue radio no longer discards your listening session on a transient network hiccup, and now reseeds from your last few played tracks instead of only the very last one, so it's less likely to repeat a song you just heard.

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
