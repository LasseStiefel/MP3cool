# Architecture decisions

## ADR-001 — Android 10 minimum

**Decision:** API 29 minimum and API 35 compile/target for the foundation. This avoids legacy support while retaining broad development-device coverage.

## ADR-002 — Kotlin contract boundary

**Decision:** `hardware-api` is Kotlin/JVM and contains no Android types. Android development behavior lives in `hardware-mock`, swapped with Hilt bindings.

## ADR-003 — Narrow-screen rail navigation

**Decision:** The prototype uses a compact persistent vertical music navigation rail rather than stock bottom navigation. It preserves vertical space and presents a dedicated-instrument character. Ergonomics must be tested on target aspect ratios before Milestone 2.

## ADR-004 — Dependencies follow milestones

**Decision:** Compose, Navigation, coroutines, and Hilt are included now. Media3 and Room are added when playback and persistence are implemented, avoiding unused production dependencies and premature schemas.

## ADR-005 — Launcher mode stays opt-in

**Decision:** Milestone 1 installs as a normal application and exposes a false build configuration constant. A production launcher intent/filter and device-owner policy require a later explicit build variant and deployment decision.

## Human input before Milestone 2

- Confirm target main-display aspect ratio/density and preferred handedness for the side rail.
- Choose whether Now Playing is a persistent global affordance or a primary navigation destination.
- Approve typography/licensing direction and whether album artwork may dominate the home screen.
- Decide whether Milestone 2 should include portrait only or adaptive landscape/tablet previews.
