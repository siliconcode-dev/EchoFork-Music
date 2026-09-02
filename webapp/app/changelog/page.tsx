import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";

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
            <article
              key={release.version}
              className="relative border-l-2 border-hairline/60 pl-6"
            >
              <div className="absolute -left-[9px] top-1 h-4 w-4 rounded-full border-2 border-canvas bg-accent" />
              <div className="flex flex-wrap items-baseline gap-3">
                <h2 className="font-display text-xl font-medium text-ink">
                  v{release.version}
                </h2>
                <span className="rounded-pill bg-accent-subtle px-2.5 py-0.5 text-xs font-medium text-accent-strong">
                  {release.tag}
                </span>
                <span className="text-xs text-muted">{release.date}</span>
              </div>
              <ul className="mt-4 space-y-2.5 text-sm leading-relaxed text-body">
                {release.highlights.map((item) => (
                  <li key={item} className="flex gap-2.5">
                    <span className="mt-2 h-1 w-1 shrink-0 rounded-full bg-accent" />
                    <span>{item}</span>
                  </li>
                ))}
              </ul>
            </article>
          ))}
        </div>

        <p className="mt-14 border-t border-hairline/60 pt-8 text-center text-sm text-muted">
          Looking for the full, unabridged history?{" "}
          <a
            href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/CHANGELOG.md"
            target="_blank"
            rel="noopener noreferrer"
            className="text-accent-strong hover:underline"
          >
            See CHANGELOG.md on GitHub
          </a>
          .
        </p>
      </div>
    </div>
  );
}
