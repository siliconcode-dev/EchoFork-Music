# Changelog

All notable changes to Enhanced Echo Music are documented here.

## [0.1.13] — 2026-09-04

### Added (Better Echo)
- Two real mini-player styles to choose from (Settings > Interface): a new ring-progress pill player, or the classic full-width bar — both ported from upstream Echo Music's real designs.
- A flip-card easter egg on the About screen — tap the app avatar.
- The Player's "more options" sheet now gets the same squircle-card treatment as the rest of Better Echo.
- The lyrics card's "Show" button is now an icon-only fullscreen button, matching upstream's visual language (still opens the same fullscreen lyrics view).

### Fixed (Better Echo Home, from v0.1.12)
- Quick Picks' hero carousel: side (peeking) items now render properly rounded and at the correct scale, instead of appearing square and downscaled — the carousel's own mask/clip modifiers are used instead of a plain corner clip, so items track its built-in scale animation correctly.
- The "Let's start with a radio" / "Quick picks" header text no longer gets cut off against the squircle card's rounded corner.
- Speed Dial no longer reserves a full grid's worth of blank space when you have only a few most-played songs — the grid now sizes itself to what you actually have.
- Home section titles (Speed Dial, Keep Listening) now match the rest of the app's heading style.

### Unaffected
- Classic and Liquid Glass are completely unchanged.

## [0.1.12] — 2026-09-04

### Added (Better Echo)
- Home's "Quick Picks" is now a hero carousel (ported from upstream Echo Music's real current design) instead of a small grid — full-bleed artwork, swipe through your radio-ready picks.
- Two new Home sections, both Better-Echo-only: **Speed Dial** (your most-played songs, in a paged grid you can swipe through) and **Keep Listening** (a real recently-played row, driven by your actual play history).
- A new "Randomize Home Order" toggle (Settings > Content, off by default) that reshuffles Home's sections each time you pull to refresh.
- Every top-level Home section now renders in the same rounded squircle-card style already used across Better Echo's Settings.

### Unaffected
- Classic and Liquid Glass Home are completely unchanged.

## [0.1.11.2] — 2026-09-04

### Fixed
- **The "iOS 26 style" nav is back, for real this time**: v0.1.11.1 only disabled it after it crashed on launch — this release properly fixes the root cause instead. The nav bar's underlying library was published as a compiled binary built against an old Compose version, which no longer matched what this app actually runs; it's now compiled directly from source (the same fix upstream Echo Music itself already shipped for this exact bug), so it can't drift out of sync again.
- **New safety net**: if an experimental nav style crashes on your device for any other reason, the app now automatically switches back to the reliable default nav bar on your next launch — no need to dig into Settings, since you might not have been able to reach them.

### Changed
- Refreshed the "What's New" dialog to reflect what's actually shipped recently: the real nav bar port, Settings' squircle-row redesign and search, the smarter Create Playlist flow, and this release's reliability work.
- Fixed a text-contrast issue on the What's New dialog's "Got It" button.

## [0.1.11.1] — 2026-09-03

### Fixed
- **Crash on launch (Better Echo)**: the default floating nav bar could crash with "Asking for measurement result of unmeasured layout modifier" on some devices — an incompatible use of intrinsic sizing under the sliding-selection-pill background. Fixed.
- **Crash when switching to the "iOS 26 style" nav** (or on every subsequent launch, for anyone who had already switched to it): threw `NoSuchMethodError` on `SharedTransitionScope.sharedElement`, caused by a version conflict in the third-party pill-nav library. That nav style is temporarily disabled (forced back to the default floating toolbar, regardless of your saved setting) until the underlying library conflict is resolved.

## [0.1.11] — 2026-09-03

### Added
- Better Echo now has a real nav bar, ported from upstream Echo Music's actual current source: the default floating toolbar with a sliding selection pill and a "More Options" sheet (Shuffle, and an AI Hub row that jumps straight to the AI settings section), plus an alternate "iOS 26 style" floating pill nav bar you can switch to from Settings > Interface.
- Fixed a bug where Better Echo's rounded nav dock (added in v0.1.8) never actually rendered on-device — the setting existed but the parameter that triggers it was never passed through.

## [0.1.10] — 2026-09-03

### Changed
- Better Echo's Library FAB now opens a menu instead of jumping straight into a playlist name field, matching upstream Echo Music's actual current flow: "Create Playlist" opens a two-tile chooser ("Normally" vs a preview of the upcoming AI-assisted option, shown as coming soon).

## [0.1.9] — 2026-09-03

### Changed
- Better Echo's Settings screen now uses upstream Echo Music's actual current row component (fetched and ported directly from its live source, not approximated): every section renders as a proper "squircle stack" card group with icon-tint boxes and badge support, matching upstream pixel-for-pixel rather than last version's shape-only approximation.
- Added a Settings search bar (Better Echo only) that jumps straight to the matching section instead of scrolling through the whole list.

## [0.1.8] — 2026-09-03

### Added
- Better Echo now has a real, consistent visual identity instead of just the Library FAB and About card from v0.1.7: a "squircle stack" row-group shape language (pulled directly from upstream's own settings-list component) applied across Settings and Library, a softer rounded lyrics panel on Now Playing, a new floating rounded nav bar, and a larger corner-radius theme token that softens buttons and small M3 components app-wide.
- A dedicated "About Enhanced Echo Music" screen in Better Echo mode: a scalloped Material 3 Expressive badge header, a wavy divider, developer credits, and a "Community & Info" card (repository, license, third-party libraries).
- Multi-select in playlist detail screens (Better Echo mode): a "Select" button lets you pick multiple songs and remove them in one action, across both local and synced YouTube playlists.
- Liquid Glass now also renders on the Settings screen's top bar, alongside its existing coverage (nav bar, mini-player, Artist/Album/Playlist, Now Playing).

### Changed
- The "What's New" dialog is updated again for this release (Better Echo's new look, multi-select, the About screen, Liquid Glass on Settings).

## [0.1.7] — 2026-09-03

### Added
- New "Interface" picker in Settings (replacing the old "Enable Liquid Glass Effect" toggle): choose between **Classic** (today's UI, unchanged), **Better Echo** (upstream's recent library/FAB/About UX changes, adapted to this fork), and **Liquid Glass** (this fork's glass-material system). Switches live, no restart. Existing Liquid Glass users are carried over automatically.
- Better Echo mode: a consolidated "Create Playlist" FAB on the Library screen, and a redesigned card-based About section with a new "What's coming next" link.

### Changed
- The "What's New" dialog now reflects recent releases (Interface modes, the Innertube scraper backend, new lyrics/canvas providers) instead of the original v0.1.1 launch list, and got a Material 3 Expressive polish: bouncier spring entrance animations and a bolder, larger title.

## [0.1.6] — 2026-09-03

### Added
- New "Scraper Backend" option in Settings > Content: an experimental Innertube-based backend for search and playback stream resolution, alongside the existing default scraper. Everything else (playlists, library, liked songs, artist follow, etc.) is unaffected regardless of which is selected. Switching takes effect after restart.

## [0.1.5] — 2026-09-03

### Added
- New Canvas Provider picker in Settings: Tidal, Apple Music, EchoMusicCanvas, and ArtistVideo, alongside the existing Spotify canvas.
- New "Artist Background Video" option: an ambient backdrop video on artist pages (Apple Music), independent of what's currently playing.

## [0.1.4] — 2026-09-03

### Added
- 4 new Lyrics providers in Settings > Lyrics: YouLyPlus, Paxsenix, KuGou, and Unison, alongside the existing SimpMusic/YouTube/LRCLIB/BetterLyrics options.

## [0.1.3] — 2026-09-02

### Fixed
- The "update available" dialog no longer offers an older release (e.g. v0.1.1) as if it were new. It previously flagged any release whose tag simply *differed* from the installed version; it now does a real newer-version comparison.

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
