import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Changelog",
  description: "What's new in each release of Enhanced Echo Music.",
};

const RELEASES = [
  {
    version: "0.1.1",
    date: "September 2026",
    tag: "First release",
    highlights: [
      "First public release of Enhanced Echo Music — its own violet identity, own Firebase project, and version history reset to reflect this fork's actual age.",
      "Spatial Audio (Beta): virtualized surround sound on supported devices, built on Android's Spatializer API.",
      "Immersive Audio Passthrough (Beta): detects your device's own Dolby engine and gets you straight to its settings.",
      "True Motion (Beta): adaptive high refresh rate, matched to your display, with manual control.",
      "Wavy playback progress indicators, in Material 3's expressive style, in the app's brand violet.",
      "A “What's New” dialog summarizing new features the first time you open the app after an update.",
    ],
  },
];

export default function ChangelogPage() {
  return (
    <div className="pb-24">
      <PageHeader
        eyebrow="Changelog"
        title="What's new"
        subtitle="Every release, in plain language. For the exact commit-level history, see CHANGELOG.md in the repo."
      />

      <div className="mx-auto max-w-2xl px-6">
        <div className="mt-12 space-y-14">
          {RELEASES.map((release) => (
            <Reveal key={release.version}>
              <article className="border-2 border-ink">
                <div className="flex flex-wrap items-baseline gap-3 border-b-2 border-ink bg-surface px-6 py-4">
                  <h2 className="font-display text-xl font-black uppercase text-ink">
                    v{release.version}
                  </h2>
                  <span className="border border-ink bg-accent px-2.5 py-0.5 font-mono text-xs font-bold uppercase text-accent-ink">
                    {release.tag}
                  </span>
                  <span className="font-mono text-xs uppercase text-muted">
                    {release.date}
                  </span>
                </div>
                <ul className="space-y-3 px-6 py-6 text-sm leading-relaxed text-body">
                  {release.highlights.map((item) => (
                    <li key={item} className="flex gap-3">
                      <span className="mt-1 font-mono text-accent">&gt;&gt;</span>
                      <span>{item}</span>
                    </li>
                  ))}
                </ul>
              </article>
            </Reveal>
          ))}
        </div>

        <p className="mt-14 border-t-2 border-ink pt-8 text-center font-mono text-xs uppercase tracking-wide text-muted">
          Looking for the full history?{" "}
          <a
            href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/CHANGELOG.md"
            target="_blank"
            rel="noopener noreferrer"
            className="text-accent underline decoration-2 underline-offset-2"
          >
            See CHANGELOG.md on GitHub
          </a>
        </p>
      </div>
    </div>
  );
}
