# Enhanced Echo Music — Design Guidelines

This fork ships **three** selectable Interface modes rather than one fixed
visual language. This document covers each, and when to reach for which.

---

## 1. The three Interface modes

Settings > Interface (`DataStoreManager.interfaceMode`,
`Values.INTERFACE_CLASSIC` / `_BETTER_ECHO` / `_LIQUID_GLASS`):

- **Classic** — the original UI this fork inherited. Untouched by default;
  only bug fixes that apply universally should touch Classic's code paths.
- **Better Echo** — a faithful port of upstream Echo Music's current
  UI/UX, adapted into this fork's own components (never copy upstream's
  Kotlin verbatim — port the visual/interaction intent into
  `echo.music.enhanced`-native composables, reusing this fork's existing
  data/repository layer rather than upstream's).
- **Liquid Glass** — this fork's own glass-material skin, layered on
  **Classic's structural layout** (not Better Echo's). Built on
  `expect/ui/LiquidGlass.kt` (`rememberBackdrop`/`layerBackdrop`, backed
  by Kyant's `io.github.kyant0:backdrop`) + `ui/component/
  LiquidGlassContainer.kt`.

New UI work must be gated to the mode it belongs to
(`if (interfaceMode == DataStoreManager.INTERFACE_BETTER_ECHO)`) — never
an ungated change to a screen shared across modes, unless it's a genuine
bug fix that should apply everywhere.

---

## 2. Better Echo's visual language

Pulled directly from upstream's own current component
(`ui/component/Material3SettingsGroup.kt`), not invented:

- **Squircle-stack rows**: each row in a group is its own `Card`
  (`elevation = 0.dp`, `surfaceContainerHigh`/`primaryContainer` when
  highlighted), 2dp gaps, per-position corner shaping — single item →
  24dp all corners; first → 24dp top / 4dp bottom; last → 4dp top / 24dp
  bottom; middle → 4dp all corners. Use `Material3SettingsGroup(interfaceMode,
  items = ...)` for any new row-list content — don't hand-roll this shape
  math again.
- **Global shape token**: `shapes.extraSmall = RoundedCornerShape(24.dp)`
  when Better Echo is active, so stock M3 components pick up the rounder
  look for free.
- **Carousels**: Material3 `HorizontalCenteredHeroCarousel`/similar —
  always use the carousel's own `Modifier.maskClip(shape)`/
  `Modifier.maskBorder(border, shape)` (from the content lambda's implicit
  `CarouselItemScope` receiver) for rounding/borders on items, never a
  plain `.clip()`/`.border()` — peeking (non-focal) items render
  square/mis-scaled otherwise (confirmed real bug, v0.1.12 → fixed v0.1.13).
- **Cookie/scallop shapes**: `ui/component/BetterEchoDecor.kt`'s
  `ScallopedShape` (hand-rolled, since this Compose Multiplatform
  material3 version predates `MaterialShapes`) — reuse/adapt its
  bump-sampling approach for any new cookie-shaped element (e.g. the
  mini-player's play/pause button) rather than writing a second one.

---

## 3. Liquid Glass's visual language

- Each screen gets its **own local** `rememberBackdrop` +
  `Modifier.layerBackdrop(backdrop)` around its main scrolling content
  (never a shared/global instance — Kyant's backdrop requires the glass
  elements to be siblings of the source, not nested inside it).
- `Material3SettingsGroup`'s Liquid Glass branch draws the same squircle
  shapes as Better Echo, just via `Modifier.liquidGlass(backdrop, shape)`
  instead of a flat color — reuse it rather than building a second glass
  row component.
- No performance fallback exists yet — if real-device testing surfaces
  jank, that's a real gap to flag, not something to silently work around.

---

## 4. Extending the design system

Before adding a new UI primitive, check `ui/component/` — this fork
already has row-group, carousel-mask, scallop-shape, and glass-container
building blocks. Copy an existing pattern rather than reaching for a
generic Material 3 default when Better Echo/Liquid Glass are involved.
