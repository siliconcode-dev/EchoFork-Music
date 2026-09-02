# Privacy Policy

_Last updated September 2026_

Enhanced Echo Music — this repository, its website, and the Android app —
is built and maintained by an independent open-source developer, not a
company. This policy covers both the website and the Android app.

## The website

The site (echofork-music.vercel.app) doesn't have accounts, forms, or
logins — there is nothing to sign up for. It uses
[Vercel Analytics](https://vercel.com/docs/analytics) for aggregated,
privacy-respecting pageview data (no cookies, no cross-site tracking, no
personal information collected). That's the extent of what the website
itself collects.

## The Android app

The app uses Firebase Crashlytics and Firebase Analytics, running against
this fork's own Firebase project — not upstream's. This means crash
reports (stack traces, device model, Android version) and basic usage
events go to this project's own Firebase console, for the sole purpose of
fixing bugs and understanding which features are actually used.

If you build the app from source, you can disable this entirely by
removing the Firebase config or commenting out the two plugin lines in
`androidApp/build.gradle.kts` — see [CONTRIBUTING.md](CONTRIBUTING.md) (or
the [Docs page](https://echofork-music.vercel.app/docs) on the website).

The app itself doesn't host any accounts. Sign-ins for optional features
(Last.fm, Spotify lyrics, OpenRouter for AI playlists) go directly from
your device to those services using your own credentials or API keys —
this project never sees or stores them.

## Third-party services

The app talks to a number of third-party services directly from your
device: YouTube Music (unofficial), Spotify (lyrics/canvas, unofficial),
LRCLIB, SponsorBlock, and optionally Last.fm, plus (as of the provider
expansion) additional lyrics and canvas sources. Each is governed by its
own privacy policy — this project has no visibility into, or control
over, what those services do with requests sent to them.

## Children's privacy

This app and website aren't directed at children under 13, and no
information is knowingly collected from them.

## Changes to this policy

This policy may be updated as the project changes. Material changes will
be reflected here with an updated date.

## Contact

Questions? Open an issue on
[GitHub](https://github.com/siliconcode-dev/EchoFork-Music/issues).
