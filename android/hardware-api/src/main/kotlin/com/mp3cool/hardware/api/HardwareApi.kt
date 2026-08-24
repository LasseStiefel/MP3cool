package com.mp3cool.hardware.api

import kotlinx.coroutines.flow.StateFlow

data class EpaperRegion(val x: Int, val y: Int, val width: Int, val height: Int) {
    init {
        require(x >= 0 && y >= 0) { "Region origin cannot be negative" }
        require(width > 0 && height > 0) { "Region dimensions must be positive" }
    }
}
sealed interface EpaperContent {
    data class Player(val title: String, val artist: String, val isPlaying: Boolean) : EpaperContent
    data class Library(val title: String, val entries: List<String>, val selectedIndex: Int) : EpaperContent
    data class Keyboard(val query: String) : EpaperContent
    data class Volume(val level: Int) : EpaperContent {
        init {
            require(level in 0..100) { "Volume must be between 0 and 100" }
        }
    }
    data object Empty : EpaperContent
}
data class EpaperState(
    val content: EpaperContent = EpaperContent.Empty,
    val awake: Boolean = true,
    val refreshCount: Long = 0,
)

interface EpaperManager {
    val state: StateFlow<EpaperState>
    suspend fun show(content: EpaperContent)
    suspend fun refresh(region: EpaperRegion? = null)
    suspend fun fullRefresh()
    suspend fun sleep()
    suspend fun wake()
}

interface HapticManager {
    fun lightClick()
    fun mediumClick()
    fun heavyClick()
    fun selectionTick()
    fun error()
    fun success()
}

sealed interface PhysicalControlEvent {
    data object VolumeUp : PhysicalControlEvent
    data object VolumeDown : PhysicalControlEvent
    data object PlayPause : PhysicalControlEvent
    data object Previous : PhysicalControlEvent
    data object Next : PhysicalControlEvent
    data object Power : PhysicalControlEvent
    data class SliderPosition(val position: Int) : PhysicalControlEvent
}

interface PhysicalControlsManager {
    val events: kotlinx.coroutines.flow.Flow<PhysicalControlEvent>
}

data class DeviceBatteryState(
    val percentage: Int,
    val charging: Boolean,
    val temperatureC: Float? = null,
    val voltage: Float? = null,
    val estimatedPlaybackHours: Float? = null,
) {
    init {
        require(percentage in 0..100) { "Battery percentage must be between 0 and 100" }
        estimatedPlaybackHours?.let {
            require(it >= 0) { "Estimated playback time cannot be negative" }
        }
    }
}

interface BatteryManager { val batteryState: StateFlow<DeviceBatteryState> }

sealed interface AudioOutput {
    data object Headphones3_5mm : AudioOutput
    data object UsbAudio : AudioOutput
    data class Bluetooth(val name: String, val address: String? = null) : AudioOutput
    data object Internal : AudioOutput
}

interface AudioRouteManager {
    val activeOutput: StateFlow<AudioOutput>
    val availableOutputs: StateFlow<List<AudioOutput>>
}

enum class UpdatePhase { IDLE, CHECKING, AVAILABLE, DOWNLOADING, READY, INSTALLING, ERROR }
data class UpdateState(val phase: UpdatePhase = UpdatePhase.IDLE, val progressPercent: Int? = null)
interface UpdateManager {
    val state: StateFlow<UpdateState>
    suspend fun checkForUpdates()
    suspend fun downloadUpdate()
    suspend fun installUpdate()
}
