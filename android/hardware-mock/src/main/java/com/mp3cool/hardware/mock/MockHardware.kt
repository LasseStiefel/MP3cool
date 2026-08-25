@file:Suppress("DEPRECATION")
package com.mp3cool.hardware.mock

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.mp3cool.hardware.api.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockEpaperManager @Inject constructor(
    private val timing: EpaperMockTiming,
) : EpaperManager {
    private val mutableState = MutableStateFlow(EpaperState())
    override val state = mutableState.asStateFlow()
    override suspend fun show(content: EpaperContent) { mutableState.update { it.copy(content = content) }; refresh() }
    override suspend fun refresh(region: EpaperRegion?) { delay(timing.partialRefreshMs); mutableState.update { it.copy(refreshCount = it.refreshCount + 1) } }
    override suspend fun fullRefresh() { delay(timing.fullRefreshMs); mutableState.update { it.copy(refreshCount = it.refreshCount + 1) } }
    override suspend fun sleep() { mutableState.update { it.copy(awake = false) } }
    override suspend fun wake() { mutableState.update { it.copy(awake = true) } }
}

data class EpaperMockTiming(
    val partialRefreshMs: Long = 150,
    val fullRefreshMs: Long = 700,
) {
    init {
        require(partialRefreshMs >= 0 && fullRefreshMs >= 0) { "Refresh latency cannot be negative" }
    }
}

@Singleton
class MockHapticManager @Inject constructor(@ApplicationContext context: Context) : HapticManager {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= 31) {
        context.getSystemService(VibratorManager::class.java).defaultVibrator
    } else { context.getSystemService(Vibrator::class.java) }
    private fun pulse(duration: Long, amplitude: Int) = vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
    override fun lightClick() = pulse(12, 70)
    override fun mediumClick() = pulse(20, 130)
    override fun heavyClick() = pulse(28, 210)
    override fun selectionTick() = pulse(8, 45)
    override fun error() = vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 35, 45, 35), -1))
    override fun success() = vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 12, 35, 22), -1))
}

@Singleton
class MockPhysicalControlsManager @Inject constructor() : PhysicalControlsManager {
    private val mutableEvents = MutableSharedFlow<PhysicalControlEvent>(extraBufferCapacity = 8)
    override val events = mutableEvents.asSharedFlow()
    fun emit(event: PhysicalControlEvent) { mutableEvents.tryEmit(event) }
}

@Singleton
class MockBatteryManager @Inject constructor() : BatteryManager {
    override val batteryState = MutableStateFlow(DeviceBatteryState(percentage = 82, charging = false)).asStateFlow()
}

@Singleton
class MockAudioRouteManager @Inject constructor() : AudioRouteManager {
    override val activeOutput = MutableStateFlow<AudioOutput>(AudioOutput.Internal).asStateFlow()
    override val availableOutputs = MutableStateFlow<List<AudioOutput>>(listOf(AudioOutput.Internal)).asStateFlow()
}

@Singleton
class FakeUpdateManager @Inject constructor() : UpdateManager {
    private val mutableState = MutableStateFlow(UpdateState())
    override val state = mutableState.asStateFlow()
    override suspend fun checkForUpdates() { mutableState.value = UpdateState(UpdatePhase.CHECKING); delay(300); mutableState.value = UpdateState() }
    override suspend fun downloadUpdate() { mutableState.value = UpdateState(UpdatePhase.ERROR) }
    override suspend fun installUpdate() { mutableState.value = UpdateState(UpdatePhase.ERROR) }
}
