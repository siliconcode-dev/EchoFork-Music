import type { Metadata } from "next";
import { Bricolage_Grotesque, Unbounded, JetBrains_Mono } from "next/font/google";
import { Analytics } from "@vercel/analytics/next";
import "./globals.css";
import Nav from "@/components/Nav";
import Footer from "@/components/Footer";

const bricolage = Bricolage_Grotesque({
  variable: "--font-sans",
  subsets: ["latin"],
  weight: ["400", "500", "600"],
});

const unbounded = Unbounded({
  variable: "--font-display",
  subsets: ["latin"],
  weight: ["400", "500", "700", "900"],
});

const jetbrainsMono = JetBrains_Mono({
  variable: "--font-mono",
  subsets: ["latin"],
  weight: ["400", "500", "700"],
});

const SITE_URL = "https://echofork-music.vercel.app";

export const metadata: Metadata = {
  metadataBase: new URL(SITE_URL),
  title: {
    default: "Enhanced Echo Music — Free Music Streaming for Android",
    template: "%s — Enhanced Echo Music",
  },
  description:
    "Enhanced Echo Music is a free, ad-free, open-source YouTube Music client for Android with Spatial Audio, offline downloads, synced lyrics, and a violet Material 3 Expressive UI.",
  icons: {
    icon: "/favicon.ico",
    apple: "/apple-touch-icon.png",
  },
  openGraph: {
    title: "Enhanced Echo Music — Free Music Streaming for Android",
    description:
      "Free, ad-free, open-source YouTube Music client for Android. Spatial Audio, offline downloads, synced lyrics.",
    url: SITE_URL,
    siteName: "Enhanced Echo Music",
    type: "website",
  },
  twitter: {
    card: "summary_large_image",
    title: "Enhanced Echo Music — Free Music Streaming for Android",
    description:
      "Free, ad-free, open-source YouTube Music client for Android. Spatial Audio, offline downloads, synced lyrics.",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${bricolage.variable} ${unbounded.variable} ${jetbrainsMono.variable} antialiased`}
      >
        <Nav />
        <main>{children}</main>
        <Footer />
        <Analytics />
      </body>
    </html>
  );
}
