package com.mp3cool.app

import com.mp3cool.hardware.api.EpaperContent
import com.mp3cool.hardware.api.EpaperManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EPaperUiController(
    private val epaperManager: EpaperManager,
) {
    private var lastContent: EpaperContent? = null
    private var lastTrackId: String? = null
    private val clockFormatter = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun command(model: DeviceUiModel) {
        val content = model.toEpaperContent()
        if (content == lastContent) return

        val trackChanged = model.context.currentTrack.id != lastTrackId
        epaperManager.show(content)
        if (model.mode == DeviceMode.SLEEP || trackChanged) {
            epaperManager.fullRefresh()
        }

        lastContent = content
        lastTrackId = model.context.currentTrack.id
    }

    private fun DeviceUiModel.toEpaperContent(): EpaperContent = when (mode) {
        DeviceMode.HOME_IDLE -> EpaperContent.Launcher(
            title = "MUSIC",
            entries = launcherEntries.map { it.label.uppercase() },
            selectedIndex = selectedLauncherIndex,
        )
        DeviceMode.APP_ACTIVE -> EpaperContent.Playback(
            title = context.currentTrack.title.uppercase(),
            artist = context.currentTrack.artist,
            isPlaying = context.isPlaying,
            primaryAction = "HOME",
            secondaryAction = "NOW PLAYING",
        )
        DeviceMode.NOW_PLAYING -> EpaperContent.Playback(
            title = context.currentTrack.title.uppercase(),
            artist = context.currentTrack.artist.uppercase(),
            isPlaying = context.isPlaying,
            primaryAction = "QUEUE",
            secondaryAction = "MORE",
        )
        DeviceMode.SEARCH -> EpaperContent.Keyboard(query = context.keyboardQuery)
        DeviceMode.SETTINGS -> EpaperContent.Settings(
            title = "SETTINGS",
            actions = listOf("BACK", "HOME"),
        )
        DeviceMode.QUICK_SETTINGS -> EpaperContent.QuickControl(
            volume = context.volume,
            output = context.outputDevice,
        )
        DeviceMode.SLEEP -> EpaperContent.Sleep(
            title = context.currentTrack.title.uppercase(),
            artist = context.currentTrack.artist,
            isPlaying = context.isPlaying,
            clock = LocalTime.now().format(clockFormatter),
            batteryPercentage = context.batteryPercentage,
            layout = sleepLayout,
        )
    }
}
