# Architecture

## Dependency direction

`app` is the composition root. It owns navigation and binds UI to use cases. Feature modules (`library`, `playback`, `spotify`, and `epaper-ui`) depend on shared domain models or hardware contracts, but not on one another's implementation. The app references each feature module as the future composition root, so every module is compiled by app builds. `hardware-api` is a Kotlin-only contract module. `hardware-mock` is an Android adapter selected by Hilt and is the only current hardware implementation.

```text
app -> features -> domain
 |                 ^
 +-> hardware-api -+
 +-> hardware-mock -> hardware-api
 +-> design-system
```

Production adapters must implement `hardware-api`; UI code must not access GPIO, SPI, UART, Linux device nodes, or vendor services. Room, Media3, and their Android services are deliberately deferred until their milestones so the foundation stays small.

## State and playback

Shared media identity and validation live in the Kotlin-only `domain` module. Long-lived component state is exposed as immutable `StateFlow`; events use `Flow`. Compose collects state at the UI boundary. Milestone 3 will place ExoPlayer and MediaSession in a foreground-capable playback service, with UI communicating through a playback contract rather than owning a player.

## Failure isolation

Hardware contracts allow unavailable peripherals to report stable fallback state. A failed e-paper or haptic adapter must not terminate playback or the main UI. Structured diagnostics and richer error types arrive with their owning features.
