export default function PhoneMockup() {
  return (
    <div className="relative mx-auto w-[260px] rounded-[36px] border-[6px] border-ink/90 bg-ink/90 p-2 shadow-[0_24px_60px_rgba(108,60,233,0.25)] sm:w-[300px]">
      <div className="overflow-hidden rounded-[26px] bg-gradient-to-b from-accent-subtle to-canvas">
        {/* Status bar */}
        <div className="flex items-center justify-between px-4 pt-3 text-[10px] text-ink/60">
          <span>9:41</span>
          <span>●●●</span>
        </div>

        {/* Now-playing skeleton */}
        <div className="flex flex-col items-center gap-4 px-6 pt-10 pb-8">
          <div className="h-40 w-40 animate-pulse rounded-2xl bg-gradient-to-br from-accent to-accent-strong shadow-lg shadow-accent/30" />
          <div className="h-3 w-32 animate-pulse rounded-full bg-ink/15" />
          <div className="h-2.5 w-20 animate-pulse rounded-full bg-ink/10" />

          <div className="mt-4 flex w-full items-center gap-1">
            {Array.from({ length: 24 }).map((_, i) => (
              <span
                key={i}
                className="h-2 flex-1 animate-pulse rounded-full bg-accent/40"
                style={{
                  animationDelay: `${i * 60}ms`,
                  opacity: i < 8 ? 0.9 : 0.35,
                }}
              />
            ))}
          </div>

          <div className="mt-2 flex items-center gap-6">
            <div className="h-4 w-4 rounded-full bg-ink/15" />
            <div className="h-6 w-6 rounded-full bg-ink/15" />
            <div className="h-10 w-10 rounded-full bg-accent" />
            <div className="h-6 w-6 rounded-full bg-ink/15" />
            <div className="h-4 w-4 rounded-full bg-ink/15" />
          </div>
        </div>
      </div>
    </div>
  );
}
