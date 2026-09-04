import {
  Headphones,
  Volume2,
  Gauge,
  AudioWaveform,
  Shuffle,
  Music2,
  Download,
  Captions,
  Car,
  Sparkles,
  Radio,
  Unlock,
  ListOrdered,
  Repeat2,
  Palette,
  ShieldCheck,
  Import,
  RefreshCw,
  type LucideIcon,
} from "lucide-react";
import Reveal from "./Reveal";

const FEATURES: {
  icon: LucideIcon;
  title: string;
  tag?: string;
  body: string;
}[] = [
  {
    icon: Palette,
    title: "Choose Your Interface",
    body: "Classic, Better Echo — an ongoing port of upstream's real UI (hero carousel Home, real nav bar, dual mini-players, multi-select) — or Liquid Glass. Switch live in Settings, no restart.",
  },
  {
    icon: Sparkles,
    title: "AI Hub",
    tag: "New",
    body: "Create a playlist from a text prompt, AI-edit any existing playlist, or turn on an auto-refreshing \"Recommended by AI\" list. Bring your own OpenRouter, Gemini, or OpenAI key.",
  },
  {
    icon: Import,
    title: "Import from Spotify",
    tag: "New",
    body: "Bring in every Spotify playlist you own or follow, plus Liked Songs — each track matched to YouTube Music automatically. From Library or Settings.",
  },
  {
    icon: RefreshCw,
    title: "In-App Auto-Updates",
    tag: "New",
    body: "Download and install updates right from the app — architecture-matched APK, live progress, and a notification when it's ready.",
  },
  {
    icon: Headphones,
    title: "Spatial Audio",
    tag: "Beta",
    body: "Virtualized surround sound via your device's spatializer, right from Audio settings.",
  },
  {
    icon: Volume2,
    title: "Immersive Audio Passthrough",
    tag: "Beta",
    body: "Detects your device's own Dolby engine and gets you straight to its settings.",
  },
  {
    icon: Gauge,
    title: "True Motion",
    tag: "Beta",
    body: "Adaptive high refresh rate, matched to your display, with manual control.",
  },
  {
    icon: AudioWaveform,
    title: "Wavy Expressive UI",
    body: "Player progress bars use Material 3's expressive wavy style.",
  },
  {
    icon: ShieldCheck,
    title: "Reliability Fallback Scraper",
    body: "An independent Innertube-based backend for search and playback, as a fallback if the default ever breaks.",
  },
  {
    icon: Shuffle,
    title: "Crossfade & Gapless",
    body: "Smooth, seamless transitions between tracks — no dead air.",
  },
  {
    icon: Music2,
    title: "High-Quality Streaming",
    body: "Up to 256kbps audio for supported accounts.",
  },
  {
    icon: ListOrdered,
    title: "Real Playlist Control",
    body: "New songs land at the top, drag to reorder, or jump one straight to the top or bottom from its menu.",
  },
  {
    icon: Repeat2,
    title: "Smarter Endless Radio",
    body: "Auto-continue reseeds from your last few tracks, not just one — less repetition, survives flaky connections.",
  },
  {
    icon: Download,
    title: "Download for Offline",
    body: "Keep your favorite tracks and playlists available with no signal.",
  },
  {
    icon: Captions,
    title: "Synced Lyrics + Canvas",
    body: "Word-by-word lyrics from 8 providers, plus a looping video background from 5 canvas sources — pick whichever works best for a track.",
  },
  {
    icon: Car,
    title: "Android Auto",
    body: "Full in-car integration for hands-free listening.",
  },
  {
    icon: Radio,
    title: "Last.fm Scrobbling",
    body: "Optional — bring your own API key to track your listening history.",
  },
  {
    icon: Unlock,
    title: "100% Free & Open Source",
    body: "GPL-3.0, no ads, no subscriptions, no monetization of any kind.",
  },
];

export default function Features() {
  return (
    <section className="border-b-2 border-ink">
      <div className="mx-auto max-w-6xl px-6 py-section">
        <Reveal className="mx-auto max-w-lg text-center">
          <p className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
            [ 03 // CAPABILITIES ]
          </p>
          <h2 className="mt-3 font-display text-2xl font-black uppercase text-ink sm:text-3xl">
            Everything you need, nothing you don&apos;t
          </h2>
        </Reveal>

        <Reveal
          staggerChildren=".feature-card-content"
          className="mt-12 grid grid-cols-1 gap-[2px] border-2 border-ink bg-ink sm:grid-cols-2 lg:grid-cols-3"
        >
          {FEATURES.map((feature) => {
            const Icon = feature.icon;
            return (
              <div key={feature.title} className="bg-canvas p-6">
                <div className="feature-card-content">
                  <div className="flex h-10 w-10 items-center justify-center border-2 border-ink bg-canvas">
                    <Icon size={18} strokeWidth={2} className="text-accent" />
                  </div>
                  <h3 className="mt-4 flex flex-wrap items-center gap-2 font-display text-sm font-bold uppercase text-ink">
                    {feature.title}
                    {feature.tag && (
                      <span className="border border-ink bg-accent px-1.5 py-0.5 font-mono text-[10px] font-bold uppercase text-accent-ink">
                        {feature.tag}
                      </span>
                    )}
                  </h3>
                  <p className="mt-2 text-sm leading-relaxed text-body">
                    {feature.body}
                  </p>
                </div>
              </div>
            );
          })}
        </Reveal>
      </div>
    </section>
  );
}
