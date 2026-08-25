package com.mp3cool.app

import com.mp3cool.hardware.api.AudioOutput

enum class DeviceMode {
    HOME_IDLE,
    APP_ACTIVE,
    NOW_PLAYING,
    SEARCH,
    SETTINGS,
    QUICK_SETTINGS,
    SLEEP,
}

enum class LauncherAction {
    OPEN_EXTERNAL_APP,
    OPEN_LOCAL_MUSIC,
    OPEN_SETTINGS,
}

enum class IconType {
    STREAMING,
    LOCAL,
    SYSTEM,
}

data class MusicAppEntry(
    val id: String,
    val label: String,
    val packageName: String?,
    val iconType: IconType,
    val action: LauncherAction,
)

data class MockTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationLabel: String,
    val positionLabel: String,
    val color: Long,
    val sampleRate: String = "44.1 kHz / 24 bit",
)

data class ActiveContext(
    val activeApp: MusicAppEntry? = null,
    val currentTrack: MockTrack = DemoMusic.currentTrack,
    val isPlaying: Boolean = true,
    val outputDevice: String = "Internal speaker",
    val batteryPercentage: Int = 82,
    val keyboardQuery: String = "",
    val volume: Int = 64,
)

data class DeviceUiModel(
    val mode: DeviceMode = DeviceMode.HOME_IDLE,
    val context: ActiveContext = ActiveContext(),
    val launcherEntries: List<MusicAppEntry> = DemoMusic.launcherEntries,
    val selectedLauncherIndex: Int = 0,
    val sleepLayout: com.mp3cool.hardware.api.SleepLayout = com.mp3cool.hardware.api.SleepLayout.ARTWORK,
)

object DemoMusic {
    val launcherEntries = listOf(
        MusicAppEntry("spotify", "Spotify", "com.spotify.music", IconType.STREAMING, LauncherAction.OPEN_EXTERNAL_APP),
        MusicAppEntry("local", "Local Music", null, IconType.LOCAL, LauncherAction.OPEN_LOCAL_MUSIC),
        MusicAppEntry("apple", "Apple Music", "com.apple.android.music", IconType.STREAMING, LauncherAction.OPEN_EXTERNAL_APP),
        MusicAppEntry("settings", "Settings", null, IconType.SYSTEM, LauncherAction.OPEN_SETTINGS),
    )

    val currentTrack = MockTrack(
        id = "night-drive",
        title = "Night Drive",
        artist = "Chromatics",
        album = "Night Drive",
        durationLabel = "04:43",
        positionLabel = "02:14",
        color = 0xFF7D2D33,
    )

    val recentlyAdded = listOf(
        MockTrack("discovery", "One More Time", "Daft Punk", "Discovery", "05:20", "00:00", 0xFFC9A227),
        MockTrack("moon-safari", "La femme d'argent", "Air", "Moon Safari", "07:08", "00:00", 0xFF39706D),
        MockTrack("teen-dream", "Zebra", "Beach House", "Teen Dream", "04:49", "00:00", 0xFF7A5C8B),
    )

    val artists = linkedMapOf(
        "A" to listOf("ABBA", "Air", "Arctic Monkeys"),
        "B" to listOf("Beach House", "Bon Iver"),
        "C" to listOf("Chromatics", "Cocteau Twins"),
        "D" to listOf("Daft Punk", "David Bowie"),
    )

    val queue = listOf(
        "Back From the Grave",
        "The Page",
        "Lady",
        "These Streets Will Never Look the Same",
        "Broken Mirrors",
    )
}

fun AudioOutput.displayName(): String = when (this) {
    AudioOutput.Headphones3_5mm -> "Wired headphones"
    AudioOutput.Internal -> "Internal speaker"
    AudioOutput.UsbAudio -> "USB DAC"
    is AudioOutput.Bluetooth -> name
}
