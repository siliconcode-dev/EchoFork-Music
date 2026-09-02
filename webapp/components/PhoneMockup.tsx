import Image from "next/image";

export default function PhoneMockup() {
  return (
    <div className="hard-shadow relative mx-auto w-[260px] border-2 border-ink bg-canvas p-2 sm:w-[300px]">
      <div className="border-2 border-ink bg-surface">
        {/* Status bar */}
        <div className="flex items-center justify-between border-b-2 border-ink px-4 py-2 font-mono text-[10px] uppercase text-ink">
          <span>09:41</span>
          <span>/// REC</span>
        </div>

        {/* Real Now Playing screenshot */}
        <div className="relative aspect-[540/1136] w-full overflow-hidden">
          <Image
            src="/screenshots/02-now-playing.webp"
            alt="Enhanced Echo Music Now Playing screen"
            fill
            sizes="(min-width: 640px) 300px, 260px"
            className="object-cover object-top"
            priority
          />
        </div>
      </div>
    </div>
  );
}
