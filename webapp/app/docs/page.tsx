import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Docs",
  description:
    "How to install and update Enhanced Echo Music, build it from source, and what its beta features actually do.",
};

function CodeBlock({ children }: { children: string }) {
  return (
    <pre className="mt-4 overflow-x-auto border-2 border-ink bg-ink p-4 font-mono text-xs leading-relaxed text-canvas">
      <code>{children}</code>
    </pre>
  );
}

function Section({
  id,
  index,
  title,
  children,
}: {
  id: string;
  index: string;
  title: string;
  children: React.ReactNode;
}) {
  return (
    <Reveal>
      <section id={id} className="mt-14 scroll-mt-24 border-t-2 border-ink pt-8">
        <p className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
          [ {index} ]
        </p>
        <h2 className="mt-2 font-display text-lg font-black uppercase text-ink">
          {title}
        </h2>
        <div className="mt-4 space-y-4 text-sm leading-relaxed text-body">
          {children}
        </div>
      </section>
    </Reveal>
  );
}

export default function DocsPage() {
  return (
    <div className="pb-24">
      <PageHeader
        eyebrow="Docs"
        title="How it works"
        subtitle="Installing, updating, building from source, and what the beta features actually do."
      />

      <div className="mx-auto max-w-3xl px-6">
        <nav className="mt-10 flex flex-wrap justify-center gap-x-6 gap-y-2 border-b-2 border-ink pb-6 font-mono text-xs uppercase tracking-wide text-muted">
          <a href="#install" className="hover:text-accent">
            Install &amp; update
          </a>
          <a href="#source" className="hover:text-accent">
            Building from source
          </a>
          <a href="#features" className="hover:text-accent">
            Feature deep-dives
          </a>
        </nav>

        <Section id="install" index="01 // SETUP" title="Install & update">
          <p>
            Enhanced Echo Music isn&apos;t on the Play Store — it&apos;s
            distributed as a signed APK directly from GitHub Releases.
          </p>
          <ol className="ml-5 list-decimal space-y-2">
            <li>
              Go to the{" "}
              <a
                href="https://github.com/siliconcode-dev/EchoFork-Music/releases/latest"
                target="_blank"
                rel="noopener noreferrer"
                className="text-accent underline decoration-2 underline-offset-2"
              >
                latest release
              </a>{" "}
              and download the APK for your device&apos;s architecture — pick{" "}
              <code className="border border-ink bg-surface px-1.5 py-0.5">
                universal
              </code>{" "}
              if you&apos;re not sure which one you need.
            </li>
            <li>
              On your phone, allow installs from unknown sources for your
              browser or file manager (Settings → Apps → Special access →
              Install unknown apps).
            </li>
            <li>Open the downloaded APK and tap Install.</li>
          </ol>
          <p>
            <strong>Updating</strong> works the same way — download a newer
            release and install over the existing app. Android treats it as
            an update as long as the signing key matches, which it will for
            any release built by this project&apos;s own CI.
          </p>
        </Section>

        <Section id="source" index="02 // BUILD" title="Building from source">
          <p>Requirements: JDK 21, Android SDK 37 (min SDK 26).</p>
          <CodeBlock>
            {`git clone https://github.com/siliconcode-dev/EchoFork-Music.git
cd EchoFork-Music
./gradlew assembleDebug`}
          </CodeBlock>
          <p>
            The APK is written to{" "}
            <code className="border border-ink bg-surface px-1.5 py-0.5">
              androidApp/build/outputs/apk/debug/
            </code>
            . Optional Last.fm scrobbling needs your own API credentials in a
            gitignored{" "}
            <code className="border border-ink bg-surface px-1.5 py-0.5">
              local.properties
            </code>{" "}
            — the feature just disables itself at runtime if they&apos;re
            missing. Full setup notes are in the repo&apos;s README.
          </p>
        </Section>

        <Section id="features" index="03 // DEEP-DIVE" title="Feature deep-dives">
          <div>
            <h3 className="font-display text-sm font-bold uppercase text-ink">
              Spatial Audio (Beta)
            </h3>
            <p className="mt-1">
              Built on Android&apos;s <code>Spatializer</code> API
              (Android 13+). Virtualizes surround sound on a
              spatializer-capable output — most modern headphones and many
              phone speakers qualify. Toggle it in Settings → Audio; it&apos;s
              hidden or shown disabled on devices/outputs that don&apos;t
              support it.
            </p>
          </div>
          <div>
            <h3 className="font-display text-sm font-bold uppercase text-ink">
              Immersive Audio Passthrough (Beta)
            </h3>
            <p className="mt-1">
              Detects whether your device has its own Dolby audio engine.
              Android&apos;s public SDK doesn&apos;t let apps directly attach
              to or control an arbitrary third-party audio effect, so instead
              of a fake toggle, this deep-links you straight to your
              device&apos;s system Sound settings, where you can manage it
              yourself if one is present.
            </p>
          </div>
          <div>
            <h3 className="font-display text-sm font-bold uppercase text-ink">
              True Motion (Beta)
            </h3>
            <p className="mt-1">
              Matches the app&apos;s refresh rate to your display&apos;s
              highest supported mode (or a manually chosen one) for smoother
              scrolling and animations. Only shows device-detected modes —
              requires Android 11+ and a display above 60Hz.
            </p>
          </div>
        </Section>
      </div>
    </div>
  );
}
