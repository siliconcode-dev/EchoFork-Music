# Security Policy

## Supported Versions

Only the latest released version of Enhanced Echo Music receives security
fixes. There is no long-term support branch — please update to the latest
release before reporting an issue.

## Reporting a Vulnerability

If you discover a security vulnerability, please **do not** open a public
issue. Instead:

1. Use GitHub's private vulnerability reporting: go to the
   [Security tab](https://github.com/siliconcode-dev/EchoFork-Music/security/advisories)
   on this repository and click **Report a vulnerability**.
2. If that isn't available to you, open a regular issue with the report
   flagged as sensitive and no exploit details included — a maintainer
   will follow up privately.

Please include as much detail as possible: affected version, steps to
reproduce, and potential impact. This is a community-maintained
open-source project without a dedicated security team, so response times
vary — but every report will be acknowledged and addressed as soon as
possible.

## Scope Notes

- This app is a client: it talks directly to YouTube Music, Spotify,
  Last.fm, and similar third-party services from the user's own device.
  Vulnerabilities in those services themselves are out of scope — report
  them to the respective provider.
- Optional integrations (Last.fm, Spotify, OpenRouter for AI playlists)
  are bring-your-own-credential: the app never stores or transmits these
  keys anywhere but directly between the user's device and that service.
- Crash and analytics data goes to this fork's own Firebase project, not
  upstream's — see [PRIVACY_POLICY.md](PRIVACY_POLICY.md) for what's
  collected.
