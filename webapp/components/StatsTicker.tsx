import { Star, Package, Unlock } from "lucide-react";
import { getRepoStats } from "@/lib/github";
import AnimatedCounter from "./AnimatedCounter";

export default async function StatsTicker() {
  const { stars, downloads } = await getRepoStats();

  return (
    <div className="border-b-2 border-ink bg-surface">
      <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-center gap-x-10 gap-y-3 px-6 py-6 font-mono text-sm font-medium uppercase tracking-wide text-body">
        <span className="flex items-center gap-2">
          <Star size={16} strokeWidth={2.5} className="text-accent" />
          <AnimatedCounter value={stars} /> GitHub Stars
        </span>
        <span className="flex items-center gap-2">
          <Package size={16} strokeWidth={2.5} className="text-accent" />
          <AnimatedCounter value={downloads} suffix="+" /> Downloads
        </span>
        <span className="flex items-center gap-2">
          <Unlock size={16} strokeWidth={2.5} className="text-accent" />
          100% Open Source
        </span>
      </div>
    </div>
  );
}
