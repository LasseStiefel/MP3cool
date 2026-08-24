# Power architecture

The conceptual states are Active, Playback Screen-off, Idle, and Deep Sleep. In screen-off playback, Media3 continues while the color UI stops unnecessary work and e-paper presents controls. In idle, Android may suspend more deeply while e-paper retains a static image. A future MCU may remain awake for touch, buttons, or wake signals, but feasibility depends on hardware.

No kernel suspend, wake-source, fuel-gauge, or battery-life claim is implemented. `BatteryManager` isolates platform/fuel-gauge state. Thermal limits, wake topology, panel power, and the playback-hours estimator require hardware measurements.
