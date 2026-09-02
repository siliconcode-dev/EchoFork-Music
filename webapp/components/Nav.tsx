import Image from "next/image";
import Link from "next/link";

const DOWNLOAD_URL =
  "https://github.com/siliconcode-dev/EchoFork-Music/releases/latest";

export default function Nav() {
  return (
    <header className="sticky top-0 z-50 border-b border-hairline/60 bg-canvas/80 backdrop-blur-md">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-3">
        <Link href="/" className="flex items-center gap-2">
          <Image
            src="/icon.png"
            alt=""
            width={32}
            height={32}
            className="rounded-lg"
          />
          <span className="font-display text-sm font-medium tracking-tight text-ink">
            Enhanced Echo Music
          </span>
        </Link>

        <nav className="hidden items-center gap-6 text-sm text-body sm:flex">
          <Link href="/docs" className="transition hover:text-ink">
            Docs
          </Link>
          <Link href="/changelog" className="transition hover:text-ink">
            Changelog
          </Link>
          <Link href="/credits" className="transition hover:text-ink">
            Credits
          </Link>
        </nav>

        <a
          href={DOWNLOAD_URL}
          target="_blank"
          rel="noopener noreferrer"
          className="ripple rounded-pill bg-accent px-5 py-2 text-sm font-medium text-white transition hover:bg-accent-strong"
        >
          Download
        </a>
      </div>
    </header>
  );
}
