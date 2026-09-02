import Image from "next/image";
import Link from "next/link";

export default function Footer() {
  return (
    <footer className="bg-surface">
      <div className="mx-auto max-w-6xl px-6 py-12">
        <div className="flex flex-col gap-10 sm:flex-row sm:justify-between">
          <div className="max-w-xs">
            <div className="flex items-center gap-2.5">
              <Image
                src="/icon.png"
                alt=""
                width={28}
                height={28}
                className="border-2 border-ink"
              />
              <span className="font-display text-sm font-medium uppercase text-ink">
                Enhanced Echo Music
              </span>
            </div>
            <p className="mt-3 text-sm leading-relaxed text-muted">
              A fork of{" "}
              <a
                href="https://github.com/iad1tya/Echo-Music"
                target="_blank"
                rel="noopener noreferrer"
                className="underline decoration-2 underline-offset-2 hover:text-accent"
              >
                Echo Music
              </a>
              , a third-party YouTube Music client. Doesn&apos;t host or hold
              any audio content.
            </p>
          </div>

          <div className="flex gap-16">
            <div>
              <div className="font-mono text-xs font-bold uppercase tracking-wide text-ink">
                Site
              </div>
              <ul className="mt-3 space-y-2 font-mono text-xs uppercase text-body">
                <li>
                  <Link href="/docs" className="hover:text-accent">
                    Docs
                  </Link>
                </li>
                <li>
                  <Link href="/changelog" className="hover:text-accent">
                    Changelog
                  </Link>
                </li>
                <li>
                  <Link href="/credits" className="hover:text-accent">
                    Credits
                  </Link>
                </li>
              </ul>
            </div>

            <div>
              <div className="font-mono text-xs font-bold uppercase tracking-wide text-ink">
                Legal
              </div>
              <ul className="mt-3 space-y-2 font-mono text-xs uppercase text-body">
                <li>
                  <Link href="/privacy" className="hover:text-accent">
                    Privacy Policy
                  </Link>
                </li>
                <li>
                  <Link href="/terms" className="hover:text-accent">
                    Terms &amp; Conditions
                  </Link>
                </li>
                <li>
                  <a
                    href="https://github.com/siliconcode-dev/EchoFork-Music/issues"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="hover:text-accent"
                  >
                    Report an issue
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div className="mt-10 flex flex-col gap-2 border-t-2 border-ink pt-6 font-mono text-[11px] uppercase tracking-wide text-muted sm:flex-row sm:items-center sm:justify-between">
          <span>
            Licensed under{" "}
            <a
              href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/LICENSE"
              target="_blank"
              rel="noopener noreferrer"
              className="underline decoration-2 underline-offset-2 hover:text-accent"
            >
              GPL-3.0
            </a>
          </span>
          <span>Not affiliated with Google or YouTube.</span>
        </div>
      </div>
    </footer>
  );
}
