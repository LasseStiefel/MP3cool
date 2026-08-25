# MP3cool

MP3cool is the Android software foundation for a dedicated portable music player with a color main display and a secondary e-paper display. The repository currently implements **Milestone 1**: a runnable, modular application shell, hardware contracts, development mocks, dependency injection, navigation, and architecture documentation.

## Requirements

- Android Studio Ladybug or newer
- JDK 17+
- Android SDK 35
- Android emulator or physical device running Android 10/API 29 or newer

Android Studio normally includes a compatible embedded JDK. In Android Studio, open
Settings/Preferences > Build, Execution, Deployment > Build Tools > Gradle and set
Gradle JDK to either the embedded JDK, if it is JDK 17 or newer, or a separately
installed JDK 17.

The checked-in Gradle wrapper downloads Gradle 8.10.2 automatically, so you do not
need to install Gradle separately.

## Build

```bash
./gradlew :android:app:assembleDebug
```

The debug APK runs as a normal application on Android 10 (API 29) or newer. Dedicated launcher behavior is intentionally deferred and represented only as a build configuration concept.

## Recommended Run Setup

The best development experience is to use Android Studio with an Android emulator.
This gives you Gradle sync, SDK installation, Logcat, Compose inspection, and device
configuration in one place.

Open the repository root in Android Studio:

```text
/Users/lassestiefel/Documents/MP3cool
```

Do not open only `android/app`. The root `settings.gradle.kts` registers all
application, domain, feature, hardware, and design-system modules. Wait for Gradle
sync to finish. Android Studio should create or update an uncommitted
`local.properties` file pointing to your Android SDK. If Android Studio asks to
install SDK Platform 35, accept the installation.

Create an emulator with:

- A compact phone profile
- API 35 system image
- Portrait orientation
- Resolution around 1080 x 2160
- 2-4 GB emulator RAM
- Hardware graphics acceleration

Cold boot the emulator once after creating it. The app supports API 29 and newer,
but API 35 matches the target SDK and exposes current platform behavior during
development.

After the emulator has booted, select the `android.app` run configuration, or create
an Android App configuration using module `MP3cool.android.app`. Select the emulator
in the device selector and click Run.

The app is declared as an ordinary launcher application, so Android Studio installs
it and opens `MainActivity`. It does not replace Android HOME in the current
development configuration because `DEDICATED_LAUNCHER_MODE` is `false`.

## Current App Shell

MP3cool is currently a Milestone 1 application shell. Navigation, styling, hardware
abstractions, and mocks are in place, but Media3 playback, Room library scanning,
Spotify control, and the full e-paper simulator are intentionally not implemented
yet.

The current shell provides a compact navigation rail with:

- Home
- Library
- Spotify
- Search
- Settings

Home and Library also link to placeholder routes for:

- Now Playing
- Recently Added
- Downloads
- Albums
- Artists
- Songs
- Playlists
- Favorites

These routes are navigable, but their full feature implementations belong to later
milestones.

## Modules

Application and Android modules live under `android/`. Hardware-independent contracts are in `hardware-api`; emulator/device implementations are in `hardware-mock`. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for dependency rules and [`docs/ROADMAP.md`](docs/ROADMAP.md) for scope.

No production GPIO, e-paper transport, audio HAL, firmware, or unofficial Spotify functionality is included.
