import Image from "next/image";

export default function ScreenshotFrame({
  label,
  src,
}: {
  label: string;
  src: string;
}) {
  return (
    <div className="flex w-[180px] shrink-0 flex-col items-center gap-3 sm:w-[200px]">
      <div className="hard-shadow-sm w-full border-2 border-ink bg-canvas p-1.5">
        <div className="relative h-[340px] w-full overflow-hidden border-2 border-ink bg-surface">
          <Image
            src={src}
            alt={`${label} screen`}
            fill
            sizes="200px"
            className="object-cover object-top"
          />
        </div>
      </div>
      <span className="font-mono text-xs font-medium uppercase tracking-wide text-muted">
        {label}
      </span>
    </div>
  );
}
