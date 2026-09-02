import { getRepoStats } from "@/lib/github";
import AnimatedCounter from "./AnimatedCounter";

export default async function StatsTicker() {
  const { stars, downloads } = await getRepoStats();

  return (
    <div className="border-y border-hairline/60 bg-surface-soft">
      <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-center gap-x-10 gap-y-3 px-6 py-6 text-sm font-medium text-body">
        <span>
          ⭐ <AnimatedCounter value={stars} /> GitHub Stars
        </span>
        <span>
          📦 <AnimatedCounter value={downloads} suffix="+" /> Downloads
        </span>
        <span>🧑‍💻 100% Open Source</span>
      </div>
    </div>
  );
}
