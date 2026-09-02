const CARD_COLORS = [
  "bg-card-violet",
  "bg-card-lilac",
  "bg-card-periwinkle",
  "bg-card-lavender-pink",
  "bg-card-soft-indigo",
  "bg-card-pale-violet",
];

const FEATURES = [
  {
    icon: "🎧",
    title: "Spatial Audio",
    tag: "Beta",
    body: "Virtualized surround sound via your device's spatializer, right from Audio settings.",
  },
  {
    icon: "🔊",
    title: "Immersive Audio Passthrough",
    tag: "Beta",
    body: "Detects your device's own Dolby engine and gets you straight to its settings.",
  },
  {
    icon: "📈",
    title: "True Motion",
    tag: "Beta",
    body: "Adaptive high refresh rate, matched to your display, with manual control.",
  },
  {
    icon: "✨",
    title: "Wavy Expressive UI",
    body: "Player progress bars use Material 3's expressive wavy style.",
  },
  {
    icon: "🔀",
    title: "Crossfade & Gapless",
    body: "Smooth, seamless transitions between tracks — no dead air.",
  },
  {
    icon: "🎵",
    title: "High-Quality Streaming",
    body: "Up to 256kbps audio for supported accounts.",
  },
  {
    icon: "⬇️",
    title: "Download for Offline",
    body: "Keep your favorite tracks and playlists available with no signal.",
  },
  {
    icon: "📝",
    title: "Synced Lyrics + Canvas",
    body: "Word-by-word lyrics, with Spotify Canvas visualizations support.",
  },
  {
    icon: "🚗",
    title: "Android Auto",
    body: "Full in-car integration for hands-free listening.",
  },
  {
    icon: "🤖",
    title: "AI Song Suggestions",
    body: "Playback-aware recommendations and automated custom playlists.",
  },
  {
    icon: "📻",
    title: "Last.fm Scrobbling",
    body: "Optional — bring your own API key to track your listening history.",
  },
  {
    icon: "🔓",
    title: "100% Free & Open Source",
    body: "GPL-3.0, no ads, no subscriptions, no monetization of any kind.",
  },
];

export default function Features() {
  return (
    <section className="mx-auto max-w-6xl px-6 py-section">
      <div className="reveal mx-auto max-w-lg text-center">
        <h2 className="font-display text-2xl font-medium text-ink sm:text-3xl">
          Everything you need, nothing you don&apos;t
        </h2>
      </div>

      <div className="mt-12 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {FEATURES.map((feature, i) => (
          <div
            key={feature.title}
            className={`reveal rounded-xl p-6 ${CARD_COLORS[i % CARD_COLORS.length]}`}
            style={{ animationDelay: `${(i % 6) * 60}ms` }}
          >
            <span className="text-2xl">{feature.icon}</span>
            <h3 className="mt-3 flex items-center gap-2 font-display text-sm font-medium text-ink">
              {feature.title}
              {feature.tag && (
                <span className="rounded-pill bg-white/60 px-2 py-0.5 text-[10px] font-medium text-accent-strong">
                  {feature.tag}
                </span>
              )}
            </h3>
            <p className="mt-2 text-sm leading-relaxed text-body">
              {feature.body}
            </p>
          </div>
        ))}
      </div>
    </section>
  );
}
