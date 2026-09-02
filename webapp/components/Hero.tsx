import PhoneMockup from "./PhoneMockup";

const DOWNLOAD_URL =
  "https://github.com/siliconcode-dev/EchoFork-Music/releases/latest";
const SOURCE_URL = "https://github.com/siliconcode-dev/EchoFork-Music";

export default function Hero() {
  return (
    <section className="relative overflow-hidden">
      <div
        className="pointer-events-none absolute inset-0"
        style={{
          background:
            "radial-gradient(circle at 50% 20%, rgba(108,60,233,0.16) 0%, rgba(108,60,233,0.05) 35%, transparent 60%)",
        }}
      />

      <div className="relative mx-auto grid max-w-6xl gap-12 px-6 py-20 sm:py-28 lg:grid-cols-2 lg:items-center">
        <div className="reveal text-center lg:text-left">
          <h1 className="font-display text-4xl font-medium leading-[1.05] tracking-tight text-ink sm:text-5xl lg:text-6xl">
            Your music,
            <br />
            <span className="text-accent">rebuilt louder.</span>
          </h1>
          <p className="mx-auto mt-6 max-w-md text-base leading-relaxed text-body lg:mx-0">
            Enhanced Echo Music is a free, open-source YouTube Music client
            for Android — Spatial Audio, adaptive refresh rate, synced
            lyrics, and a violet Material 3 Expressive UI. No ads, no
            subscriptions, ever.
          </p>

          <div className="mt-8 flex flex-col items-center gap-3 sm:flex-row sm:justify-center lg:justify-start">
            <a
              href={DOWNLOAD_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="ripple w-full rounded-pill bg-accent px-7 py-3 text-center text-sm font-medium text-white shadow-lg shadow-accent/25 transition hover:bg-accent-strong sm:w-auto"
            >
              Download APK
            </a>
            <a
              href={SOURCE_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="ripple w-full rounded-pill border border-hairline bg-white/60 px-7 py-3 text-center text-sm font-medium text-ink transition hover:bg-white sm:w-auto"
            >
              View Source Code
            </a>
          </div>

          <p className="mt-4 text-xs text-muted">
            Android 8.0+ · Sideloaded APK, not on the Play Store
          </p>
        </div>

        <div className="reveal" style={{ animationDelay: "120ms" }}>
          <PhoneMockup />
        </div>
      </div>
    </section>
  );
}
