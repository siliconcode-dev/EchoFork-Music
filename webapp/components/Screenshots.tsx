import ScreenshotFrame from "./ScreenshotFrame";
import Reveal from "./Reveal";

const SCREENS: { label: string; src: string }[] = [
  { label: "Home", src: "/screenshots/01-home.webp" },
  { label: "Now Playing", src: "/screenshots/02-now-playing.webp" },
  { label: "Synced Lyrics", src: "/screenshots/03-lyrics.webp" },
  { label: "Search", src: "/screenshots/04-search.webp" },
  { label: "Library", src: "/screenshots/05-library.webp" },
  { label: "Playlist", src: "/screenshots/06-playlist.webp" },
  { label: "Song Menu", src: "/screenshots/08-song-menu.webp" },
  { label: "Settings", src: "/screenshots/09-settings.webp" },
  { label: "Spatial Audio", src: "/screenshots/10-settings-audio.webp" },
  { label: "Appearance", src: "/screenshots/11-settings-interface.webp" },
  { label: "Endless Queue", src: "/screenshots/12-queue.webp" },
  { label: "Artist", src: "/screenshots/13-artist.webp" },
  { label: "Album", src: "/screenshots/14-album.webp" },
  { label: "Mini Player", src: "/screenshots/15-mini-player.webp" },
];

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
            Real screens, straight off the device.
          </p>
        </Reveal>

        <Reveal
          staggerChildren=":scope > div"
          className="mt-12 flex gap-6 overflow-x-auto pb-4 sm:justify-center sm:flex-wrap"
        >
          {SCREENS.map((screen) => (
            <ScreenshotFrame
              key={screen.label}
              label={screen.label}
              src={screen.src}
            />
          ))}
        </Reveal>
      </div>
    </section>
  );
}
