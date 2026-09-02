const VARIANTS = {
  "now-playing": (
    <div className="flex flex-col items-center gap-3 px-5 pt-8">
      <div className="h-28 w-28 rounded-2xl bg-gradient-to-br from-accent to-accent-strong" />
      <div className="h-2.5 w-24 rounded-full bg-ink/15" />
      <div className="h-2 w-16 rounded-full bg-ink/10" />
      <div className="mt-3 h-1.5 w-full rounded-full bg-accent/30" />
    </div>
  ),
  library: (
    <div className="flex flex-col gap-2.5 px-5 pt-8">
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} className="flex items-center gap-2.5">
          <div className="h-9 w-9 shrink-0 rounded-lg bg-accent-subtle" />
          <div className="flex-1">
            <div className="h-2 w-3/4 rounded-full bg-ink/15" />
            <div className="mt-1.5 h-1.5 w-1/2 rounded-full bg-ink/10" />
          </div>
        </div>
      ))}
    </div>
  ),
  search: (
    <div className="flex flex-col gap-2.5 px-5 pt-8">
      <div className="h-8 w-full rounded-pill border border-hairline bg-white/70" />
      <div className="mt-2 grid grid-cols-3 gap-2">
        {Array.from({ length: 6 }).map((_, i) => (
          <div
            key={i}
            className="aspect-square rounded-lg bg-accent-subtle"
          />
        ))}
      </div>
    </div>
  ),
  lyrics: (
    <div className="flex flex-col items-center gap-2.5 px-5 pt-10 text-center">
      {[100, 80, 90, 60, 85].map((w, i) => (
        <div
          key={i}
          className="h-2.5 rounded-full"
          style={{
            width: `${w}%`,
            background: i === 1 ? "var(--color-accent)" : "rgba(10,10,10,0.1)",
          }}
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
    <div className="reveal flex w-[180px] shrink-0 flex-col items-center gap-3 sm:w-[200px]">
      <div className="w-full rounded-[28px] border-[5px] border-ink/90 bg-ink/90 p-1.5 shadow-[0_16px_32px_rgba(0,0,0,0.1)]">
        <div className="h-[340px] overflow-hidden rounded-[20px] bg-gradient-to-b from-accent-tint to-canvas">
          {VARIANTS[variant]}
        </div>
      </div>
      <span className="text-xs font-medium text-muted">{label}</span>
    </div>
  );
}
