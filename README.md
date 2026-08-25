# MP3cool

MP3cool is the Android software foundation for a dedicated portable music player with a color main display and a secondary e-paper display. The repository currently implements **Milestone 1**: a runnable, modular application shell, hardware contracts, development mocks, dependency injection, navigation, and architecture documentation.

## Requirements

- Android Studio Ladybug or newer
- JDK 17+
- Android SDK 35

## Build

```bash
./gradlew :android:app:assembleDebug
```

The debug APK runs as a normal application on Android 10 (API 29) or newer. Dedicated launcher behavior is intentionally deferred and represented only as a build configuration concept.

## Run in Android Studio

1. Clone the repository and open the **repository root** (the directory containing
   `settings.gradle.kts`) in Android Studio. Do not open `android/app` by itself.
2. Allow Gradle sync to finish and install Android SDK 35 if prompted.
3. Create or select an Android 10+ emulator in **Tools > Device Manager**.
4. Select the shared **MP3cool** run configuration and click **Run**.

The shared run configuration is stored in `.run/MP3cool.run.xml`. If it does not
appear, use **File > Sync Project with Gradle Files**, then close and reopen the
project root. As a manual fallback, add an **Android App** configuration, select
the `MP3cool.android.app` module, and choose **Default Activity** as the launch
mode.

## Modules

Application and Android modules live under `android/`. Hardware-independent contracts are in `hardware-api`; emulator/device implementations are in `hardware-mock`. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for dependency rules and [`docs/ROADMAP.md`](docs/ROADMAP.md) for scope.

No production GPIO, e-paper transport, audio HAL, firmware, or unofficial Spotify functionality is included.
