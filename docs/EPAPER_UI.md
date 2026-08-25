# E-paper UI

E-paper is a primary, page-oriented interface when the color display sleeps. UI modes will include playback, library, keyboard, volume, and idle/status. Screens use high contrast, fixed regions, large targets, discrete selection, and no animation or smooth scrolling.

Partial refreshes update only changed regions after a configurable delay. Full refreshes are less frequent and clear accumulated ghosting. Exact timing, resolution, waveform constraints, touch behavior, and full-refresh cadence remain simulator settings until a panel is selected. Progress and volume update in deliberate steps rather than continuously.

The simulator is scheduled for Milestone 6. The semantic `EpaperManager` contract exists now so production UI will not depend on that simulator.
