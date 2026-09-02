import { ImageResponse } from "next/og";
import { readFile } from "node:fs/promises";
import { join } from "node:path";

export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default async function Image() {
  const iconData = await readFile(join(process.cwd(), "public", "icon.png"));
  const iconSrc = `data:image/png;base64,${iconData.toString("base64")}`;

  return new ImageResponse(
    (
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          background:
            "radial-gradient(circle at 50% 40%, #7c4bf0 0%, #4a1fb0 55%, #150b2e 100%)",
        }}
      >
        {/* eslint-disable-next-line @next/next/no-img-element */}
        <img
          src={iconSrc}
          width={160}
          height={160}
          style={{ borderRadius: 32 }}
          alt=""
        />
        <div
          style={{
            marginTop: 36,
            fontSize: 60,
            fontWeight: 700,
            color: "#ffffff",
            letterSpacing: -1,
          }}
        >
          Enhanced Echo Music
        </div>
        <div
          style={{
            marginTop: 14,
            fontSize: 28,
            color: "rgba(255,255,255,0.75)",
          }}
        >
          Free, ad-free YouTube Music client for Android
        </div>
      </div>
    ),
    { ...size },
  );
}
