"use client";

import { useEffect, useRef, useState } from "react";
import { animate } from "animejs";

export default function AnimatedCounter({
  value,
  prefix = "",
  suffix = "",
  durationMs = 1400,
}: {
  value: number;
  prefix?: string;
  suffix?: string;
  durationMs?: number;
}) {
  const [display, setDisplay] = useState(0);
  const ref = useRef<HTMLSpanElement>(null);
  const hasRun = useRef(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    const reveal = () => {
      if (hasRun.current) return;
      hasRun.current = true;

      const counter = { value: 0 };
      animate(counter, {
        value,
        round: 1,
        duration: durationMs,
        ease: "outExpo",
        onUpdate: () => setDisplay(counter.value),
      });
    };

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (!entry.isIntersecting) return;
        reveal();
        observer.disconnect();
      },
      { threshold: 0.4, rootMargin: "0px 0px 200px 0px" },
    );

    observer.observe(el);
    const fallback = window.setTimeout(reveal, 1200);

    return () => {
      observer.disconnect();
      window.clearTimeout(fallback);
    };
  }, [value, durationMs]);

  return (
    <span ref={ref} className="font-mono tabular-nums">
      {prefix}
      {display.toLocaleString()}
      {suffix}
    </span>
  );
}
