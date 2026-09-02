import Image from "next/image";
import Link from "next/link";

export default function Footer() {
  return (
    <footer className="border-t border-hairline/60 bg-surface-soft">
      <div className="mx-auto max-w-6xl px-6 py-12">
        <div className="flex flex-col gap-10 sm:flex-row sm:justify-between">
          <div className="max-w-xs">
            <div className="flex items-center gap-2">
              <Image
                src="/icon.png"
                alt=""
                width={28}
                height={28}
                className="rounded-lg"
              />
              <span className="font-display text-sm font-medium text-ink">
                Enhanced Echo Music
              </span>
            </div>
            <p className="mt-3 text-sm leading-relaxed text-muted">
              A fork of{" "}
              <a
                href="https://github.com/iad1tya/Echo-Music"
                target="_blank"
                rel="noopener noreferrer"
                className="underline decoration-hairline underline-offset-2 hover:text-ink"
              >
                Echo Music
              </a>
              , a third-party YouTube Music client. Doesn&apos;t host or hold
              any audio content.
            </p>
          </div>

          <div className="flex gap-16">
            <div>
              <div className="font-display text-xs font-medium uppercase tracking-wide text-muted">
                Site
              </div>
              <ul className="mt-3 space-y-2 text-sm text-body">
                <li>
                  <Link href="/docs" className="hover:text-ink">
                    Docs
                  </Link>
                </li>
                <li>
                  <Link href="/changelog" className="hover:text-ink">
                    Changelog
                  </Link>
                </li>
                <li>
                  <Link href="/credits" className="hover:text-ink">
                    Credits
                  </Link>
                </li>
              </ul>
            </div>

            <div>
              <div className="font-display text-xs font-medium uppercase tracking-wide text-muted">
                Legal
              </div>
              <ul className="mt-3 space-y-2 text-sm text-body">
                <li>
                  <Link href="/privacy" className="hover:text-ink">
                    Privacy Policy
                  </Link>
                </li>
                <li>
                  <Link href="/terms" className="hover:text-ink">
                    Terms &amp; Conditions
                  </Link>
                </li>
                <li>
                  <a
                    href="https://github.com/siliconcode-dev/EchoFork-Music/issues"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="hover:text-ink"
                  >
                    Report an issue
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div className="mt-10 flex flex-col gap-2 border-t border-hairline/60 pt-6 text-xs text-muted sm:flex-row sm:items-center sm:justify-between">
          <span>
            Licensed under{" "}
            <a
              href="https://github.com/siliconcode-dev/EchoFork-Music/blob/main/LICENSE"
              target="_blank"
              rel="noopener noreferrer"
              className="underline decoration-hairline underline-offset-2 hover:text-ink"
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
