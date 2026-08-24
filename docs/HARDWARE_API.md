# Hardware API

The Kotlin-only `hardware-api` module defines e-paper, haptic, physical-control, battery, audio-route, and update contracts. Development implementations live in `hardware-mock` and are replaceable through dependency injection.

E-paper content is semantic rather than transport-specific. No resolution, GPIO, bus, controller, waveform, or microcontroller is assumed. Regions use abstract pixel coordinates whose valid bounds will be supplied by a future capability description.

## Proposed future MCU protocol

A future framed, versioned, checksummed protocol may carry `SET_FRAME`, `SET_REGION`, `REFRESH_PARTIAL`, `REFRESH_FULL`, `SET_HAPTIC`, `TOUCH_EVENT`, `BUTTON_EVENT`, `SLEEP`, and `WAKE`. Transport (USB, UART, SPI, or a platform service), payload encoding, acknowledgement rules, retry behavior, security boundaries, panel resolution, and MCU remain undecided. Production work must negotiate capabilities rather than infer them.

Physical slider positions are integers at the boundary; their product meaning is user-configurable and not embedded in the driver. Haptic methods express intent, not a specific motor or driver.
