"use client";

import { useLayoutEffect, useRef } from "react";
import { animate, stagger } from "animejs";

export default function Reveal({
  children,
  className,
  staggerChildren,
  delay = 0,
}: {
  children: React.ReactNode;
  className?: string;
  /** CSS selector for direct children to stagger-animate individually. */
  staggerChildren?: string;
  delay?: number;
}) {
  const ref = useRef<HTMLDivElement>(null);
  const hasRun = useRef(false);

  useLayoutEffect(() => {
    const el = ref.current;
    if (!el) return;

    const targets: HTMLElement | NodeListOf<Element> = staggerChildren
      ? el.querySelectorAll(staggerChildren)
      : el;

    const targetList =
      targets instanceof NodeList ? Array.from(targets) : [targets];
    targetList.forEach((t) => {
      (t as HTMLElement).style.opacity = "0";
    });

    const reveal = () => {
      if (hasRun.current) return;
      hasRun.current = true;

      animate(targets, {
        opacity: [0, 1],
        translateY: [24, 0],
        duration: 700,
        delay: staggerChildren ? stagger(70, { start: delay }) : delay,
        ease: "outExpo",
      });
    };

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (!entry.isIntersecting) return;
        reveal();
        observer.disconnect();
      },
      { threshold: 0.15, rootMargin: "0px 0px 200px 0px" },
    );

    observer.observe(el);

    // Safety net: some capture/render paths (full-page screenshot tools,
    // slow JS, non-standard viewports) never fire the observer. Never leave
    // content permanently invisible.
    const fallback = window.setTimeout(reveal, 1200);

    return () => {
      observer.disconnect();
      window.clearTimeout(fallback);
    };
  }, [staggerChildren, delay]);

  return (
    <div ref={ref} className={className}>
      {children}
    </div>
  );
}
