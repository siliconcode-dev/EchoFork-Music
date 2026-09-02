import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import LegalSection from "@/components/LegalSection";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Privacy Policy",
  description:
    "What data this website and the Enhanced Echo Music Android app collect, and what they don't.",
};

export default function PrivacyPage() {
  return (
    <div className="pb-24">
      <PageHeader eyebrow="Legal" title="Privacy Policy" />

      <div className="mx-auto max-w-2xl px-6">
        <p className="mt-10 font-mono text-xs uppercase tracking-wide text-muted">
          Last updated September 2026
        </p>

        <Reveal>
          <LegalSection title="Overview">
            <p>
              Enhanced Echo Music — this website and the Android app — is
              built and maintained by an independent open-source developer,
              not a company. This policy covers both: what this website
              collects, and what the Android app collects.
            </p>
          </LegalSection>

          <LegalSection title="This website">
            <p>
              This site doesn&apos;t have accounts, forms, or logins — there
              is nothing to sign up for. It uses{" "}
              <a
                href="https://vercel.com/docs/analytics"
                target="_blank"
                rel="noopener noreferrer"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                Vercel Analytics
              </a>{" "}
              for aggregated, privacy-respecting pageview data (no cookies,
              no cross-site tracking, no personal information collected).
              That&apos;s the extent of what this website itself collects.
            </p>
          </LegalSection>

          <LegalSection title="The Android app">
            <p>
              The app uses Firebase Crashlytics and Firebase Analytics,
              running against this fork&apos;s own Firebase project — not
              upstream&apos;s. This means crash reports (stack traces, device
              model, Android version) and basic usage events go to this
              project&apos;s own Firebase console, for the sole purpose of
              fixing bugs and understanding which features are actually
              used.
            </p>
            <p>
              If you build the app from source, you can disable this
              entirely by removing the Firebase config or commenting out the
              two plugin lines in{" "}
              <code className="border border-ink bg-surface px-1.5 py-0.5">
                androidApp/build.gradle.kts
              </code>{" "}
              — see the{" "}
              <a
                href="/docs#source"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                Docs
              </a>{" "}
              page.
            </p>
            <p>
              The app itself doesn&apos;t host any accounts. Sign-ins for
              optional features (Last.fm, Spotify lyrics) go directly from
              your device to those services using your own credentials or
              API keys — this project never sees or stores them.
            </p>
          </LegalSection>

          <LegalSection title="Third-party services">
            <p>
              The app talks to a number of third-party services directly
              from your device: YouTube Music (unofficial), Spotify
              (lyrics/canvas, unofficial), LRCLIB, SponsorBlock, and
              optionally Last.fm. Each is governed by its own privacy policy
              — this project has no visibility into, or control over, what
              those services do with requests sent to them.
            </p>
          </LegalSection>

          <LegalSection title="Children's privacy">
            <p>
              This app and website aren&apos;t directed at children under
              13, and no information is knowingly collected from them.
            </p>
          </LegalSection>

          <LegalSection title="Changes to this policy">
            <p>
              This policy may be updated as the project changes. Material
              changes will be reflected here with an updated date.
            </p>
          </LegalSection>

          <LegalSection title="Contact">
            <p>
              Questions? Open an issue on{" "}
              <a
                href="https://github.com/siliconcode-dev/EchoFork-Music/issues"
                target="_blank"
                rel="noopener noreferrer"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                GitHub
              </a>
              .
            </p>
          </LegalSection>
        </Reveal>
      </div>
    </div>
  );
}
