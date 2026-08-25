# Audio architecture

Milestone 3 will use Media3 ExoPlayer plus MediaSession in a service independent of Compose. Android continues to own Bluetooth A2DP and USB audio. A future production route may use Android I2S through a platform audio HAL to a dedicated DAC/amplifier, but no HAL or component is selected.

`AudioRouteManager` exposes product-level outputs without coupling UI to Android device objects. Route discovery and platform restrictions will be implemented later. Codec labels are informational and must come from measured platform state, not assumptions.
