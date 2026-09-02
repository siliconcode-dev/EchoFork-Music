"use client";

import { useState } from "react";

const FAQ_ITEMS = [
  {
    q: "Is Enhanced Echo Music free?",
    a: "Yes — 100% free, no ads, no subscriptions, no monetization of any kind. It's licensed under GPL-3.0 and always will be.",
  },
  {
    q: "Does it host or store any music?",
    a: "No. It's a client for publicly available YouTube Music content — no audio or video is hosted, uploaded, or stored by this project. All content stays on Google/YouTube's servers.",
  },
  {
    q: "Is it available on the Play Store?",
    a: "Not currently. Download the signed APK directly from this project's GitHub Releases.",
  },
  {
    q: "How do I install it?",
    a: "Enable “Install from unknown sources” for your browser or file manager, then open the downloaded APK. Full step-by-step instructions are on the Docs page.",
  },
  {
    q: "How do I get updates?",
    a: "The app checks its own GitHub Releases feed for updates. You can also watch the repo or re-download the latest APK whenever a new version ships.",
  },
  {
    q: "Is this affiliated with Google, YouTube, or the upstream Echo Music project?",
    a: "No. It's an independent, GPL-3.0 fork of Echo Music by iad1tya — see the Credits page for full attribution to Echo Music and SimpMusic, the projects this fork builds on.",
  },
  {
    q: "Is my data safe?",
    a: "There are no accounts hosted by this project. Crash and analytics telemetry goes to this fork's own Firebase project, not upstream's. See the Privacy Policy for details.",
  },
];

export default function Faq() {
  const [openIndex, setOpenIndex] = useState<number | null>(0);

  return (
    <section className="mx-auto max-w-3xl px-6 py-section">
      <h2 className="reveal text-center font-display text-2xl font-medium text-ink sm:text-3xl">
        Straight answers
      </h2>

      <div className="mt-10 divide-y divide-hairline/60 rounded-xl border border-hairline/60 bg-surface-card/40">
        {FAQ_ITEMS.map((item, i) => {
          const isOpen = openIndex === i;
          return (
            <div key={item.q}>
              <button
                type="button"
                onClick={() => setOpenIndex(isOpen ? null : i)}
                className="flex w-full items-center justify-between gap-4 px-6 py-4 text-left"
                aria-expanded={isOpen}
              >
                <span className="font-display text-sm font-medium text-ink">
                  {item.q}
                </span>
                <span
                  className={`shrink-0 text-accent transition-transform ${isOpen ? "rotate-45" : ""}`}
                >
                  +
                </span>
              </button>
              {isOpen && (
                <p className="px-6 pb-5 text-sm leading-relaxed text-body">
                  {item.a}
                </p>
              )}
            </div>
          );
        })}
      </div>
    </section>
  );
}
