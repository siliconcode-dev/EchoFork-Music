import Skeleton from "./Skeleton";

export default function PhoneMockup() {
  return (
    <div className="hard-shadow relative mx-auto w-[260px] border-2 border-ink bg-canvas p-2 sm:w-[300px]">
      <div className="border-2 border-ink bg-surface">
        {/* Status bar */}
        <div className="flex items-center justify-between border-b-2 border-ink px-4 py-2 font-mono text-[10px] uppercase text-ink">
          <span>09:41</span>
          <span>/// REC</span>
        </div>

        {/* Now-playing skeleton */}
        <div className="flex flex-col items-center gap-4 px-6 pb-8 pt-8">
          <Skeleton className="h-40 w-40 border-2 border-ink" />
          <Skeleton className="h-3 w-32" />
          <Skeleton className="h-2.5 w-20" delay={100} />

          <div className="mt-4 flex w-full items-end gap-1">
            {Array.from({ length: 20 }).map((_, i) => (
              <Skeleton
                key={i}
                delay={i * 60}
                className="w-full"
                style={{ height: `${8 + ((i * 37) % 24)}px` }}
              />
            ))}
          </div>

          <div className="mt-3 flex items-center gap-6">
            <div className="h-3 w-3 border-2 border-ink" />
            <div className="h-5 w-5 border-2 border-ink" />
            <div className="h-9 w-9 border-2 border-ink bg-accent" />
            <div className="h-5 w-5 border-2 border-ink" />
            <div className="h-3 w-3 border-2 border-ink" />
          </div>
        </div>
      </div>
    </div>
  );
}
