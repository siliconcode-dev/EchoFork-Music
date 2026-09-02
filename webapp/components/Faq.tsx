"use client";

import { useState } from "react";
import { Plus, X } from "lucide-react";
import Reveal from "./Reveal";

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
    <section className="border-b-2 border-ink">
      <div className="mx-auto max-w-3xl px-6 py-section">
        <Reveal className="text-center">
          <p className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
            [ 04 // FAQ ]
          </p>
          <h2 className="mt-3 font-display text-2xl font-black uppercase text-ink sm:text-3xl">
            Straight answers
          </h2>
        </Reveal>

        <Reveal className="mt-10 divide-y-2 divide-ink border-2 border-ink">
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
                  <span className="font-display text-sm font-bold uppercase text-ink">
                    {item.q}
                  </span>
                  <span className="flex h-6 w-6 shrink-0 items-center justify-center border-2 border-ink bg-accent text-accent-ink">
                    {isOpen ? (
                      <X size={14} strokeWidth={3} />
                    ) : (
                      <Plus size={14} strokeWidth={3} />
                    )}
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
        </Reveal>
      </div>
    </section>
  );
}
