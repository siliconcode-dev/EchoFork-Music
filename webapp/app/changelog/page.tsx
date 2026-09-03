import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Changelog",
  description: "What's new in each release of Enhanced Echo Music.",
};

const RELEASES = [
  {
    version: "0.1.7",
    date: "September 2026",
    tag: "New",
    highlights: [
      "New \"Interface\" picker in Settings, replacing the old Liquid Glass toggle: choose Classic (today's UI), Better Echo (upstream's recent library/FAB/About redesign, adapted to this fork), or Liquid Glass (this fork's glass-material look). Switches live, no restart, and existing Liquid Glass users are carried over automatically.",
      "Better Echo mode adds a consolidated \"Create Playlist\" button on the Library screen and a redesigned, card-based About section with a \"What's coming next\" link.",
      "The \"What's New\" dialog — which had quietly kept showing the original v0.1.1 launch list on every update since — now reflects what's actually new, with a livelier Material 3 Expressive entrance animation.",
    ],
  },
  {
    version: "0.1.6",
    date: "September 2026",
    tag: "New",
    highlights: [
      "New \"Scraper Backend\" option in Settings: an experimental Innertube-based backend for search and playback, as a reliability fallback alongside the existing default. Everything else — playlists, library, liked songs, artist follow — works the same either way. Switching takes effect after restart.",
    ],
  },
  {
    version: "0.1.5",
    date: "September 2026",
    tag: "New",
    highlights: [
      "New Canvas Provider picker in Settings: Tidal and Apple Music join Spotify as sources for the looping video background on the Now Playing screen, plus an experimental community-submitted option.",
      "New \"Artist Background Video\" setting: an ambient backdrop video on artist pages, independent of what's currently playing.",
    ],
  },
  {
    version: "0.1.4",
    date: "September 2026",
    tag: "New",
    highlights: [
      "4 new Lyrics providers in Settings: YouLyPlus, Paxsenix, KuGou, and Unison, alongside the existing SimpMusic, YouTube, LRCLIB, and BetterLyrics options — more chances of finding synced lyrics for obscure or regional tracks.",
    ],
  },
  {
    version: "0.1.3",
    date: "September 2026",
    tag: "Fix",
    highlights: [
      "The \"update available\" dialog no longer offers an older release as if it were new — it previously flagged any release whose tag simply differed from the installed version, even a downgrade.",
    ],
  },
  {
    version: "0.1.2",
    date: "September 2026",
    tag: "Fixes",
    highlights: [
      "Adding a song to a playlist now puts it at the top instead of burying it at the bottom — synced YouTube playlists stay in sync too.",
      "Playlist reordering no longer requires switching to Custom Order sort first, and a real bug is fixed where dragging a song a long distance could scramble the rest of the list.",
      "Each song's menu now has “Move to top” / “Move to bottom” — a precise way to reorder without dragging.",
      "Endless-queue radio survives a flaky connection instead of losing your session, and now reseeds from your last few played tracks instead of just one — less repetition.",
    ],
  },
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
