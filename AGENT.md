# AGENT.md — Enhanced Echo Music

Context file for AI agents (Claude Code, etc.) working in this repo. Keep
this file up to date as the project evolves — it's the fastest way to give
an agent full context without re-scanning the whole codebase every session.

## Development Rules & Guidelines

> **Maintenance rule:** whenever you add or change a feature, structure,
> module, build config, or convention in this repo, **update this file in
> the same PR/commit**. This file is only useful if it stays accurate —
> stale context is worse than no context, because the agent will act on it
> confidently and be wrong. Treat an out-of-date `AGENT.md` as a bug.

> **Attribution rule:** if a feature is ported from, adapted from, or
> inspired by upstream Echo Music (or any other open-source project) —
> even partially, a UI pattern, a whole file — check `README.md`'s
> **Acknowledgements** section reflects it. Be specific about what was
> taken, not just "inspiration." Never port/adapt code without crediting
> the source, even for small snippets.

> **`CHANGELOG.md` rule:** whenever you ship a version slice, add an entry
> under a new `## [x.y.z]` heading before bumping `gradle/libs.versions.toml`.

> **Do not push without explicit instruction.** Making code changes
> (editing files, committing locally) is fine whenever asked, but **never
> run `git push`, create a tag, or publish a GitHub Release — unless
> explicitly told to** in that message ("push this", "commit and push",
> "tag and release this"). Finishing a change or being asked to "commit"
> is not by itself permission to push, tag, or release. If unsure, stop
> and ask rather than assuming.

> **Verify via real compile logs.** After any Kotlin/Compose change, run
> `./gradlew :composeApp:compileAndroidMain` (add `:module:compileAndroidMain`
> for other touched modules) and read the actual log for `BUILD SUCCESSFUL`
> — never rely solely on a tool's own success summary, and never pass `-q`
> (it hides the per-task log lines needed to tell a real failure from a
> stale-cache one).

### UI rule: Interface modes, not one visual language

This fork ships **three** selectable Interface modes (Settings > Interface):
**Classic** (upstream's pre-overhaul UI, byte-for-byte unless a bug fix
applies to it directly), **Better Echo** (a faithful port of upstream
Echo Music's current UI/UX, adapted into this fork's own components — see
the version-slice plan below), and **Liquid Glass** (this fork's own
glass-material skin layered on Classic's structural layout). New UI work
must be scoped to the mode it actually belongs to, gated on
`interfaceMode`/`DataStoreManager.INTERFACE_*` — never a blind, ungated
change to a screen all three modes share.

### Conventions worth following

- Kotlin official style: `val` over `var`, data classes for simple holders,
  sealed classes/interfaces for UI/playback state.
- **Explicit per-symbol imports everywhere** — this codebase does not rely
  on IDE auto-import or wildcard imports. A generated Compose resource
  (`Res.string.x`), an icon extension property (`echoIcons.X`), or even
  this project's own `DataStoreManager.Values` companion constant all
  require their own explicit `import` line. An "Unresolved reference" on
  a symbol that genuinely exists (confirmed in generated/compiled output)
  is almost always a missing import, not Gradle/build-cache staleness —
  check imports before reaching for `--no-configuration-cache` or a clean
  build.
- New dependency versions go in `gradle/libs.versions.toml`
  (`[versions]` + `[libraries]`), referenced via `libs.xxx` — don't
  hardcode version strings in module `build.gradle.kts` files.
- New cross-cutting integrations (a lyrics source, a canvas provider, an
  AI service) → a new Gradle module under `core/service/`, not a package
  inside `composeApp`.
- Room schema changes require a real migration — this fork avoids adding
  new entity columns where a DataStore preference will do instead, to
  sidestep migrations for simple per-user settings.
- **Networking:** Ktor (CIO/OkHttp engine per target) +
  kotlinx.serialization throughout — no OkHttp/org.json, even when a
  ported upstream feature used them.

## What this app is

Enhanced Echo Music is a **Kotlin Multiplatform / Compose Multiplatform**
fork of [Echo Music](https://github.com/iad1tya/Echo-Music) (itself built
on [SimpMusic](https://github.com/maxrave-dev/SimpMusic)) — an
ad-free YouTube Music streaming client with offline downloads, synced
lyrics (7 providers), canvas-style video backgrounds, local media
playback, Spotify import, an AI Hub (OpenRouter-backed playlist
generation/editing/recommendations), and more. Android-only in practice
(iOS targets were removed; JVM/desktop scaffolding remains for a possible
future). Package/application ID: `echo.music.enhanced`.

## Tech stack

- **Language:** Kotlin (KMP), Gradle Kotlin DSL.
- **UI:** Compose Multiplatform (Material 3 Expressive), `haze` for blur,
  `materialKolor`/`kmpalette` for dynamic/palette-extracted color, Coil 3.
- **Architecture:** MVVM — ViewModels (`composeApp/.../viewModel/`) +
  Compose screens (`composeApp/.../ui/screen/`) + a repository layer
  (`core/domain` interfaces, `core/data` impls). Koin for DI
  (`core/data/.../di/`, `composeApp/.../di/`).
- **Persistence:** Room, DataStore Preferences (`DataStoreManager`/`Impl`).
- **Playback:** Media3/ExoPlayer (`core/media/`).
- **Networking:** Ktor + kotlinx.serialization.
- **AI:** `core/service/aiService` (`AiClient`/`AiService`, an
  OpenAI-API-compatible client via `org.simpmusic.gemini-kotlin` —
  supports Gemini, OpenAI, Custom-OpenAI, and OpenRouter as one shared
  provider setting across lyrics translation and the AI Hub playlist
  features).
- **Other:** Firebase Analytics/Crashlytics (this fork's own project, not
  upstream's), a real Cast implementation.

## Module map

| Module | Purpose |
|---|---|
| `composeApp` | Main application — UI, ViewModels, DI, navigation |
| `androidApp` | Android application shell |
| `core/domain` | Repository interfaces, entities, DataStoreManager interface |
| `core/data` | Repository implementations, DataStoreManagerImpl, DI modules |
| `core/media` | Media3/ExoPlayer playback |
| `core/service/kotlinYtmusicScraper`, `core/service/innertube` | The two YT Music scraper backends |
| `core/service/aiService` | AI client (translation + AI Hub playlist features) |
| `core/service/<lyrics providers>` | youlyplus, paxsenixlyrics, kugou, unison, simpmusic, betterlyrics |
| `core/service/<canvas providers>` | canvas, applecanvas, echomusiccanvas, artistvideo |
| `core/service/spotify` | Spotify integration |

When adding a new external integration, the existing pattern is: **new
Gradle module** under `core/service/`, register it in `settings.gradle.kts`,
add it as an `implementation(projects.name)` in `core/data/build.gradle.kts`.

## Commit message format (required)

Every commit title **must** follow Conventional Commits style:

```
<type>(<scope>): <short, imperative summary>
```

- **type**: `feat`, `fix`, `build`, `chore`, `refactor`, `docs`, `perf`, `test`, or `ci`.
- **scope**: the module/area touched, lowercase (e.g. `home`, `player`, `ai`, `settings`).
- **summary**: imperative mood, no trailing period.

## Config & secrets

- `google-services.json` — this fork's own Firebase project config.
- Never commit: `local.properties`, `*.keystore`, real `google-services.json`,
  any `gradle.properties` containing signing credentials.
- AI features (lyrics translation + AI Hub playlist generation/editing/
  recommendations) are configured **in-app** (Settings > AI) — one shared
  BYO-credential API key, no build-time secret needed. OpenRouter is the
  provider the AI Hub playlist features require; Gemini/OpenAI/Custom are
  translation-only unless the model you pick also does chat completions.

## CI

- `.github/workflows/*.yml` — build + tag-triggered release (4 APK
  variants: universal, arm64-v8a, armeabi-v7a, x86_64).
