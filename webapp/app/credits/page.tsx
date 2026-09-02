import type { Metadata } from "next";
import PageHeader from "@/components/PageHeader";

export const metadata: Metadata = {
  title: "Credits",
  description:
    "Attribution for the projects Enhanced Echo Music builds on, the open-source libraries it uses, and its contributors.",
};

const LIBRARIES = [
  { name: "Jetpack Compose", note: "UI toolkit, Material 3 Expressive" },
  { name: "AndroidX Media3 (ExoPlayer)", note: "audio/video playback" },
  { name: "Kotlin Multiplatform", note: "shared business logic" },
  { name: "Koin", note: "dependency injection" },
  { name: "Room", note: "local structured storage" },
  { name: "DataStore", note: "user preferences" },
  { name: "Ktor", note: "networking" },
  { name: "Coil", note: "image loading" },
  { name: "Navigation Compose", note: "in-app navigation" },
  { name: "WorkManager", note: "background work" },
  { name: "Material Kolor / kmpalette", note: "dynamic color theming" },
  { name: "Google Cast / MediaRouter", note: "casting support" },
  { name: "Android Auto (Car App Library)", note: "in-car integration" },
  { name: "AboutLibraries", note: "in-app open-source license screen" },
];

export default function CreditsPage() {
  return (
    <div className="pb-24">
      <PageHeader
        eyebrow="Credits"
        title="Standing on real work"
        subtitle="Enhanced Echo Music didn't start from a blank slate. Here's who and what it builds on."
      />

      <div className="mx-auto max-w-3xl px-6">
        <section className="mt-12">
          <h2 className="font-display text-lg font-medium text-ink">
            Upstream projects
          </h2>
          <div className="mt-4 space-y-4">
            <div className="rounded-xl border border-hairline/60 bg-surface-card/40 p-5">
              <a
                href="https://github.com/iad1tya/Echo-Music"
                target="_blank"
                rel="noopener noreferrer"
                className="font-display text-sm font-medium text-accent-strong hover:underline"
              >
                Echo Music
              </a>
              <p className="mt-1 text-sm text-muted">
                by Aditya (
                <a
                  href="https://github.com/iad1tya"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:text-ink"
                >
                  @iad1tya
                </a>
                )
              </p>
              <p className="mt-2 text-sm leading-relaxed text-body">
                The direct upstream of this fork — Enhanced Echo Music started
                as, and continues to build on, Aditya&apos;s work.
              </p>
            </div>

            <div className="rounded-xl border border-hairline/60 bg-surface-card/40 p-5">
              <a
                href="https://github.com/maxrave-dev/SimpMusic"
                target="_blank"
                rel="noopener noreferrer"
                className="font-display text-sm font-medium text-accent-strong hover:underline"
              >
                SimpMusic
              </a>
              <p className="mt-1 text-sm text-muted">
                by{" "}
                <a
                  href="https://github.com/maxrave-dev"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:text-ink"
                >
                  maxrave-dev
                </a>
              </p>
              <p className="mt-2 text-sm leading-relaxed text-body">
                The foundation Echo Music itself builds on. Both projects are
                GPL-3.0, and this fork continues under the same license.
              </p>
            </div>
          </div>

          <p className="mt-6 text-sm leading-relaxed text-body">
            If you find this software valuable, please consider supporting
            the <strong>upstream</strong> developers, who did the bulk of
            this work — links are in{" "}
            <a
              href="https://github.com/siliconcode-dev/EchoFork-Music#acknowledgements"
              target="_blank"
              rel="noopener noreferrer"
              className="text-accent-strong hover:underline"
            >
              the repo&apos;s README
            </a>
            .
          </p>
        </section>

        <section className="mt-14">
          <h2 className="font-display text-lg font-medium text-ink">
            Open-source libraries
          </h2>
          <p className="mt-2 text-sm text-muted">
            The major libraries this app is built on. The app itself also
            ships an in-app open-source license screen (via AboutLibraries)
            with the complete list.
          </p>
          <ul className="mt-6 grid grid-cols-1 gap-x-8 gap-y-3 sm:grid-cols-2">
            {LIBRARIES.map((lib) => (
              <li
                key={lib.name}
                className="flex items-baseline justify-between gap-4 border-b border-hairline/60 pb-2 text-sm"
              >
                <span className="font-medium text-ink">{lib.name}</span>
                <span className="text-right text-muted">{lib.note}</span>
              </li>
            ))}
          </ul>
        </section>

        <section className="mt-14">
          <h2 className="font-display text-lg font-medium text-ink">
            Contributors
          </h2>
          <p className="mt-2 text-sm leading-relaxed text-body">
            This fork is maintained by{" "}
            <a
              href="https://github.com/siliconcode-dev"
              target="_blank"
              rel="noopener noreferrer"
              className="text-accent-strong hover:underline"
            >
              siliconcode-dev
            </a>
            . Contributions are welcome — see the{" "}
            <a
              href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/CONTRIBUTING.md"
              target="_blank"
              rel="noopener noreferrer"
              className="text-accent-strong hover:underline"
            >
              contributing guide
            </a>{" "}
            to get started. The full, up-to-date contributor list lives on{" "}
            <a
              href="https://github.com/siliconcode-dev/EchoFork-Music/graphs/contributors"
              target="_blank"
              rel="noopener noreferrer"
              className="text-accent-strong hover:underline"
            >
              GitHub
            </a>
            .
          </p>
        </section>
      </div>
    </div>
  );
}
