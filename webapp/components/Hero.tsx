import { Download, Code2 } from "lucide-react";
import PhoneMockup from "./PhoneMockup";
import Reveal from "./Reveal";

const DOWNLOAD_URL =
  "https://github.com/siliconcode-dev/EchoFork-Music/releases/latest/download/androidApp-universal-release.apk";
const SOURCE_URL = "https://github.com/siliconcode-dev/EchoFork-Music";

export default function Hero() {
  return (
    <section className="border-b-2 border-ink">
      <div className="mx-auto grid max-w-6xl gap-12 px-6 py-20 sm:py-28 lg:grid-cols-2 lg:items-center">
        <Reveal className="text-center lg:text-left">
          <p className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
            [ REV 0.1.16 // ANDROID 8.0+ ]
          </p>
          <h1 className="mt-4 font-display text-4xl font-black uppercase leading-[0.95] tracking-tight text-ink sm:text-5xl lg:text-6xl">
            Your music,
            <br />
            <span className="text-accent">rebuilt louder.</span>
          </h1>
          <p className="mx-auto mt-6 max-w-md text-base leading-relaxed text-body lg:mx-0">
            Enhanced Echo Music is a free, open-source YouTube Music client
            for Android — three selectable interfaces, an AI Hub, Spotify
            import, Spatial Audio, and a violet Material 3 Expressive UI. No
            ads, no subscriptions, ever.
          </p>

          <div className="mt-8 flex flex-col items-center gap-3 sm:flex-row sm:justify-center lg:justify-start">
            <a
              href={DOWNLOAD_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="press hard-shadow flex w-full items-center justify-center gap-2 border-2 border-ink bg-accent px-7 py-3 font-mono text-sm font-bold uppercase tracking-wide text-accent-ink sm:w-auto"
            >
              <Download size={16} strokeWidth={2.5} />
              Download APK
            </a>
            <a
              href={SOURCE_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="press hard-shadow flex w-full items-center justify-center gap-2 border-2 border-ink bg-canvas px-7 py-3 font-mono text-sm font-bold uppercase tracking-wide text-ink sm:w-auto"
            >
              <Code2 size={16} strokeWidth={2.5} />
              Source Code
            </a>
          </div>

          <p className="mt-4 font-mono text-[11px] uppercase tracking-wide text-muted">
            Sideloaded APK — not on the Play Store
          </p>
        </Reveal>

        <Reveal delay={150}>
          <PhoneMockup />
        </Reveal>
      </div>
    </section>
  );
}
