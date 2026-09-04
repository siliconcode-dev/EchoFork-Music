import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";
import Reveal from "@/components/Reveal";

export const metadata: Metadata = {
  title: "Changelog",
  description: "What's new in each release of Enhanced Echo Music.",
};

const RELEASES = [
  {
    version: "0.1.16",
    date: "September 2026",
    tag: "New",
    highlights: [
      "New Now Playing backdrop styles in Settings → Interface (Better Echo): Gradient (today's look, unchanged), Blur, an animated Glow, and Artwork Blend — extended to the queue sheet too.",
      "The Now Playing seek bar is back to a single, draggable Material 3 Expressive wavy bar — the stacked buffering-indicator-over-progress bug is fixed.",
      "The app can now download and install its own updates instead of sending you to the browser: \"Install Now\" or \"Install Later\" after a check, live download progress in Settings → About Us, and a notification when it's ready — automatically picks the right APK for your device's CPU architecture.",
      "\"Import from Spotify\" is now also reachable from Settings → Account, alongside the existing Library entry point.",
      "Better Echo Home: the Moods & Genres chip rows and Chart's video carousel no longer show a partially cut-off card at the edge of their rounded container.",
    ],
  },
  {
    version: "0.1.15",
    date: "September 2026",
    tag: "New",
    highlights: [
      "The play/pause button is now a rotating \"cookie\" shape that spins while playing and settles into a plain circle when paused, with a spring press-scale — matching upstream's latest transport control design.",
      "Previous/next buttons now sit inside translucent circle backgrounds, with shuffle/repeat moved into their own row below the main transport controls.",
      "Library's Import menu now has a real \"Import from Spotify\" action: brings in every playlist you own or follow, plus your Liked Songs, as local playlists, with a live progress dialog.",
      "Each Spotify track is matched to a real YouTube Music song by title, artist, and duration — reuses your existing Spotify session from Canvas/Lyrics, and re-running the import refreshes previously-imported playlists instead of duplicating them.",
    ],
  },
  {
    version: "0.1.14",
    date: "September 2026",
    tag: "New",
    highlights: [
      "AI Hub: describe what you want in Library's \"Create with AI\" tile, and a real OpenRouter-backed AI builds a playlist from your actual search results — no more \"coming soon.\"",
      "Any local playlist's \"more options\" now has a \"Modify with AI\" action — describe the change, AI adds or removes songs for you.",
      "New Settings → AI toggle creates (and lets you refresh) a \"Recommended by AI\" playlist built from your most-played songs.",
      "OpenRouter joins Gemini, OpenAI, and Custom-OpenAI as a selectable AI provider — required for the three AI Hub features above, since they share the same provider/key as lyrics translation.",
      "A couple of long-standing Better Echo bugs fixed too: overlapping Home section header text, and an imperfect ring-progress circle on the mini-player.",
    ],
  },
  {
    version: "0.1.13",
    date: "September 2026",
    tag: "New",
    highlights: [
      "Two real mini-player styles to choose from in Settings → Interface: a new ring-progress pill player, or the classic full-width bar — both ported from upstream's real designs.",
      "A flip-card easter egg on the About screen — tap the app avatar.",
      "The Player's \"more options\" sheet and the lyrics card's \"Show\" button now match Better Echo's squircle-card visual language.",
      "Fixes carried over from v0.1.12's new Home sections: hero carousel side items, header text clipping, Speed Dial's blank space, and section title styling.",
    ],
  },
  {
    version: "0.1.12",
    date: "September 2026",
    tag: "New",
    highlights: [
      "Home's \"Quick Picks\" is now a hero carousel — ported from upstream's real current design — instead of a small grid.",
      "Two new Better-Echo-only Home sections: Speed Dial (your most-played songs in a swipeable paged grid) and Keep Listening (a real recently-played row from your actual play history).",
      "New \"Randomize Home Order\" toggle in Settings → Content (off by default) reshuffles Home's sections on pull-to-refresh.",
      "Every top-level Home section now renders in the same rounded squircle-card style already used across Better Echo's Settings.",
    ],
  },
  {
    version: "0.1.11.2",
    date: "September 2026",
    tag: "Fix",
    highlights: [
      "The \"iOS 26 style\" nav is back for real this time — root-caused by vendoring the nav library's source instead of relying on a stale prebuilt binary.",
      "New safety net: if an experimental nav style crashes on your device for any other reason, the app now automatically switches back to the reliable default nav bar on your next launch.",
      "Refreshed the \"What's New\" dialog to reflect what's actually shipped recently.",
    ],
  },
  {
    version: "0.1.11.1",
    date: "September 2026",
    tag: "Fix",
    highlights: [
      "Fixed a launch crash (Better Echo) affecting the default floating nav bar on some devices.",
      "Temporarily disabled the \"iOS 26 style\" nav after it was found to crash on switch — the real fix landed in v0.1.11.2.",
    ],
  },
  {
    version: "0.1.11",
    date: "September 2026",
    tag: "New",
    highlights: [
      "Better Echo gets a real nav bar, ported from upstream's actual current source: the default floating toolbar with a sliding selection pill and a \"More Options\" sheet (Shuffle, AI Hub shortcut), plus an alternate \"iOS 26 style\" floating pill nav bar you can switch to in Settings → Interface.",
    ],
  },
  {
    version: "0.1.10",
    date: "September 2026",
    tag: "Changed",
    highlights: [
      "Better Echo's Library FAB now opens a menu instead of jumping straight into a playlist name field: \"Create Playlist\" opens a two-tile chooser for a normal playlist or an AI-assisted one.",
    ],
  },
  {
    version: "0.1.9",
    date: "September 2026",
    tag: "New",
    highlights: [
      "Better Echo's Settings screen now uses upstream's actual current row component: every section renders as a proper squircle-stack card group with icon-tint boxes and badge support.",
      "Added a Settings search bar (Better Echo only) that jumps straight to the matching section.",
    ],
  },
  {
    version: "0.1.8",
    date: "September 2026",
    tag: "New",
    highlights: [
      "Better Echo gets a real, consistent visual identity: a squircle-stack row-group shape language across Settings and Library, a softer rounded lyrics panel on Now Playing, a new floating rounded nav bar, and a larger corner-radius theme token app-wide.",
      "A dedicated \"About Enhanced Echo Music\" screen in Better Echo mode: a scalloped Material 3 Expressive badge header, a wavy divider, developer credits, and a \"Community & Info\" card.",
      "Multi-select in playlist detail screens (Better Echo mode).",
      "Liquid Glass now also renders on the Settings screen's top bar.",
    ],
  },
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
