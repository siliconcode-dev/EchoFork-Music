import Skeleton from "./Skeleton";

const VARIANTS = {
  "now-playing": (
    <div className="flex flex-col items-center gap-3 px-5 pt-8">
      <Skeleton className="h-28 w-28 border-2 border-ink" />
      <Skeleton className="h-2.5 w-24" />
      <Skeleton className="h-2 w-16" delay={80} />
      <Skeleton className="mt-3 h-1.5 w-full bg-accent" delay={120} />
    </div>
  ),
  library: (
    <div className="flex flex-col gap-2.5 px-5 pt-8">
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} className="flex items-center gap-2.5">
          <Skeleton
            className="h-9 w-9 shrink-0 border-2 border-ink"
            delay={i * 60}
          />
          <div className="flex-1">
            <Skeleton className="h-2 w-3/4" delay={i * 60} />
            <Skeleton className="mt-1.5 h-1.5 w-1/2" delay={i * 60 + 40} />
          </div>
        </div>
      ))}
    </div>
  ),
  search: (
    <div className="flex flex-col gap-2.5 px-5 pt-8">
      <div className="h-8 w-full border-2 border-ink bg-canvas" />
      <div className="mt-2 grid grid-cols-3 gap-2">
        {Array.from({ length: 6 }).map((_, i) => (
          <Skeleton
            key={i}
            className="aspect-square border-2 border-ink"
            delay={i * 50}
          />
        ))}
      </div>
    </div>
  ),
  lyrics: (
    <div className="flex flex-col items-center gap-2.5 px-5 pt-10 text-center">
      {[100, 80, 90, 60, 85].map((w, i) => (
        <Skeleton
          key={i}
          delay={i * 70}
          className={i === 1 ? "h-2.5 bg-accent" : "h-2.5"}
          style={{ width: `${w}%` }}
        />
      ))}
    </div>
  ),
} as const;

export default function ScreenshotFrame({
  label,
  variant,
}: {
  label: string;
  variant: keyof typeof VARIANTS;
}) {
  return (
    <div className="flex w-[180px] shrink-0 flex-col items-center gap-3 sm:w-[200px]">
      <div className="hard-shadow-sm w-full border-2 border-ink bg-canvas p-1.5">
        <div className="h-[340px] overflow-hidden border-2 border-ink bg-surface">
          {VARIANTS[variant]}
        </div>
      </div>
      <span className="font-mono text-xs font-medium uppercase tracking-wide text-muted">
        {label}
      </span>
    </div>
  );
}
