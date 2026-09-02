import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import LegalSection from "@/components/LegalSection";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Terms & Conditions",
  description:
    "Terms of use for Enhanced Echo Music — free, open-source, and not affiliated with Google or YouTube.",
};

export default function TermsPage() {
  return (
    <div className="pb-24">
      <PageHeader eyebrow="Legal" title="Terms & Conditions" />

      <div className="mx-auto max-w-2xl px-6">
        <p className="mt-10 font-mono text-xs uppercase tracking-wide text-muted">
          Last updated September 2026
        </p>

        <Reveal>
          <LegalSection title="1. Free & open source">
            <p>
              Enhanced Echo Music is a 100% free, open-source (GPL-3.0)
              application built for educational purposes and personal use.
              This fork contains no advertisements, no premium tier, no
              subscriptions, and no monetization of any kind.
            </p>
          </LegalSection>

          <LegalSection title="2. How it works">
            <p>
              The app functions as a specialized client that parses publicly
              available content and APIs of YouTube and YouTube Music,
              displaying them in a custom interface. It does not modify or
              bypass content protections.
            </p>
          </LegalSection>

          <LegalSection title="3. No hosted content">
            <p>
              This app does not host, upload, or store any audio, video, or
              copyrighted media. All content remains hosted on
              Google/YouTube&apos;s servers and is the property of its
              respective owners.
            </p>
          </LegalSection>

          <LegalSection title="4. Third-party services">
            <p>
              The app talks to a number of third-party services, some via
              official APIs (Last.fm, LRCLIB, SponsorBlock) and some via
              unofficial or reverse-engineered endpoints (YouTube Music,
              Spotify, Apple Music lyrics). Availability of those endpoints
              is outside this project&apos;s control and may break without
              notice. Using them may conflict with those platforms&apos; own
              Terms of Service — that choice, and the accounts you sign in
              with, are yours.
            </p>
          </LegalSection>

          <LegalSection title="5. Support creators">
            <p>
              We respect the work of artists and content creators. Users are
              encouraged to subscribe to{" "}
              <a
                href="https://www.youtube.com/premium"
                target="_blank"
                rel="noopener noreferrer"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                YouTube Premium
              </a>{" "}
              to directly support the creators they listen to. This app is a
              developer proof-of-concept, not a way to reduce creator
              revenue.
            </p>
          </LegalSection>

          <LegalSection title="6. User responsibility">
            <p>
              This software is provided &ldquo;AS IS,&rdquo; without
              warranty of any kind. Users are solely responsible for
              ensuring their use complies with local copyright laws and
              platform Terms of Service. No media is hosted by this project,
              so it cannot process DMCA takedowns for audio/video content.
            </p>
          </LegalSection>

          <LegalSection title="7. Not affiliated">
            <p>
              This project is not affiliated with, endorsed by, or connected
              to Google, YouTube, or their subsidiaries in any way.
            </p>
          </LegalSection>

          <LegalSection title="8. License">
            <p>
              Enhanced Echo Music is licensed under{" "}
              <a
                href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/LICENSE"
                target="_blank"
                rel="noopener noreferrer"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                GPL-3.0
              </a>
              , inherited from Echo Music and SimpMusic — see{" "}
              <a
                href="/credits"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                Credits
              </a>
              .
            </p>
          </LegalSection>
        </Reveal>
      </div>
    </div>
  );
}
