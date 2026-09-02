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
          background: "#0a0a0a",
        }}
      >
        <div
          style={{
            display: "flex",
            border: "6px solid #6c3ce9",
            padding: "56px 72px",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          {/* eslint-disable-next-line @next/next/no-img-element */}
          <img
            src={iconSrc}
            width={140}
            height={140}
            style={{ border: "4px solid #ffffff" }}
            alt=""
          />
          <div
            style={{
              marginTop: 32,
              fontSize: 58,
              fontWeight: 700,
              color: "#ffffff",
              letterSpacing: -1,
              textTransform: "uppercase",
            }}
          >
            Enhanced Echo Music
          </div>
          <div
            style={{
              marginTop: 16,
              fontSize: 24,
              color: "#6c3ce9",
              fontFamily: "monospace",
              textTransform: "uppercase",
              letterSpacing: 2,
            }}
          >
            {"[ FREE // AD-FREE // OPEN SOURCE ]"}
          </div>
        </div>
      </div>
    ),
    { ...size },
  );
}
