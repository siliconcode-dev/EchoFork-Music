import Image from "next/image";
import Link from "next/link";
import { Download } from "lucide-react";

const DOWNLOAD_URL =
  "https://github.com/siliconcode-dev/EchoFork-Music/releases/latest";

export default function Nav() {
  return (
    <header className="sticky top-0 z-50 border-b-2 border-ink bg-canvas">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-3">
        <Link href="/" className="flex items-center gap-2.5">
          <Image
            src="/icon.png"
            alt=""
            width={32}
            height={32}
            className="border-2 border-ink"
          />
          <span className="font-display text-sm font-medium uppercase tracking-tight text-ink">
            Enhanced Echo Music
          </span>
        </Link>

        <nav className="hidden items-center gap-6 font-mono text-xs font-medium uppercase tracking-wide text-body sm:flex">
          <Link href="/docs" className="transition hover:text-accent">
            Docs
          </Link>
          <Link href="/changelog" className="transition hover:text-accent">
            Changelog
          </Link>
          <Link href="/credits" className="transition hover:text-accent">
            Credits
          </Link>
        </nav>

        <a
          href={DOWNLOAD_URL}
          target="_blank"
          rel="noopener noreferrer"
          className="press hard-shadow-sm flex items-center gap-1.5 border-2 border-ink bg-accent px-4 py-2 font-mono text-xs font-bold uppercase tracking-wide text-accent-ink"
        >
          <Download size={14} strokeWidth={2.5} />
          Download
        </a>
      </div>
    </header>
  );
}
