# Setup Instructions

Instructions for setting up Enhanced Echo Music for local development. For
the contribution workflow itself (branching, PRs), see
[`CONTRIBUTING.md`](CONTRIBUTING.md).

## Prerequisites

- Android Studio (latest stable) or IntelliJ IDEA with the Kotlin
  Multiplatform plugin
- Android SDK 37
- JDK 21
- Git

## Initial Setup

### 1. Clone the repository

```bash
git clone https://github.com/siliconcode-dev/EchoFork-Music.git
cd EchoFork-Music
```

A plain clone is sufficient — the `core/*` modules are vendored directly
in this repository, not tracked as submodules.

### 2. Configure local properties

Create `local.properties` with your Android SDK path:

```properties
sdk.dir=/path/to/your/android/sdk
```

### 3. Configure Firebase (optional)

Firebase powers analytics and crash reporting against this fork's own
project. If you want these enabled locally:

1. Create a Firebase project at the [Firebase Console](https://console.firebase.google.com/).
2. Add an Android app with application id `echo.music.enhanced`.
3. Download `google-services.json` and place it in the `composeApp/` (or
   `androidApp/`) directory per the module that consumes it.

**The app builds and runs fine without it** — analytics/crash reporting
are simply disabled.

### 4. Configure release signing (optional)

Only needed for release builds:

```bash
export STORE_PASSWORD=your_store_password
export KEY_ALIAS=your_key_alias
export KEY_PASSWORD=your_key_password
```

Never commit a real keystore or these values into `gradle.properties`.

### 5. Build the project

```bash
# Debug build
./gradlew assembleDebug

# Verify a specific module compiles (fast inner-loop check)
./gradlew :composeApp:compileAndroidMain

# Release build (requires signing configuration above)
./gradlew assembleRelease
```

### 6. Configure AI features (optional)

The AI Hub (Create/Modify playlist with AI, AI Recommendations) and AI
lyrics translation share one provider/key setting, configured in-app at
**Settings > AI**:

- **AI Hub playlist features require OpenRouter specifically** — get an
  API key from [OpenRouter](https://openrouter.ai/), set Provider to
  OpenRouter, and enter your key. The app never stores or proxies this
  key anywhere but directly between your device and OpenRouter.
- Lyrics translation additionally supports Gemini, OpenAI, or a Custom
  OpenAI-compatible endpoint as alternative providers, if you don't need
  the AI Hub playlist features.

## Important files

### Never commit

- `local.properties`
- `google-services.json` (real project credentials)
- `*.keystore`
- `gradle.properties` if it contains signing credentials

All of the above are already in `.gitignore`.

## Troubleshooting

**"SDK location not found"** — create `local.properties` with the
correct `sdk.dir`.

**Firebase-related build errors** — build without `google-services.json`;
Firebase features are optional and disable gracefully.

**Gradle sync issues** — `./gradlew clean` then rebuild. Avoid `-q` when
diagnosing a real failure — it hides the per-task log lines that
distinguish a genuine compile error from stale cache state.

## Contributing

See [`CONTRIBUTING.md`](CONTRIBUTING.md) for the branching/PR workflow
and [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md).

## License

GNU General Public License v3.0 — see [`LICENSE`](LICENSE).
