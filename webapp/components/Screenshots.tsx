import ScreenshotFrame from "./ScreenshotFrame";
import Reveal from "./Reveal";

export default function Screenshots() {
  return (
    <section className="border-b-2 border-ink">
      <div className="mx-auto max-w-6xl px-6 py-section">
        <Reveal className="text-center">
          <p className="font-mono text-xs font-medium uppercase tracking-widest text-accent">
            [ 02 // INTERFACE ]
          </p>
          <h2 className="mt-3 font-display text-2xl font-black uppercase text-ink sm:text-3xl">
            Built for the way you listen
          </h2>
          <p className="mx-auto mt-3 max-w-md font-mono text-xs uppercase tracking-wide text-muted">
            Real screenshots incoming — this is a preview of the layout.
          </p>
        </Reveal>

        <Reveal
          staggerChildren=":scope > div"
          className="mt-12 flex gap-6 overflow-x-auto pb-4 sm:justify-center"
        >
          <ScreenshotFrame label="Now Playing" variant="now-playing" />
          <ScreenshotFrame label="Library" variant="library" />
          <ScreenshotFrame label="Search" variant="search" />
          <ScreenshotFrame label="Lyrics" variant="lyrics" />
        </Reveal>
      </div>
    </section>
  );
}
