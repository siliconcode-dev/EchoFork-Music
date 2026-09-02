import ScreenshotFrame from "./ScreenshotFrame";

export default function Screenshots() {
  return (
    <section className="mx-auto max-w-6xl px-6 py-section">
      <h2 className="reveal text-center font-display text-2xl font-medium text-ink sm:text-3xl">
        Built for the way you listen
      </h2>
      <p className="reveal mx-auto mt-3 max-w-md text-center text-sm text-muted">
        Real screenshots are coming soon — this is a preview of the layout.
      </p>

      <div className="mt-12 flex gap-6 overflow-x-auto pb-4 sm:justify-center">
        <ScreenshotFrame label="Now Playing" variant="now-playing" />
        <ScreenshotFrame label="Library" variant="library" />
        <ScreenshotFrame label="Search" variant="search" />
        <ScreenshotFrame label="Lyrics" variant="lyrics" />
      </div>
    </section>
  );
}
