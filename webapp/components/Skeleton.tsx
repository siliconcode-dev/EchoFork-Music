"use client";

import { useEffect, useRef } from "react";
import { animate } from "animejs";

export default function Skeleton({
  className,
  delay = 0,
  style,
}: {
  className?: string;
  delay?: number;
  style?: React.CSSProperties;
}) {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;

    const anim = animate(el, {
      opacity: [0.2, 0.55],
      duration: 900,
      delay,
      loop: true,
      alternate: true,
      ease: "inOutSine",
    });

    return () => {
      anim.pause();
    };
  }, [delay]);

  return <div ref={ref} className={`bg-ink ${className ?? ""}`} style={style} />;
}
