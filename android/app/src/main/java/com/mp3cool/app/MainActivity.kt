package com.mp3cool.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mp3cool.designsystem.Mp3CoolTheme
import com.mp3cool.hardware.api.AudioRouteManager
import com.mp3cool.hardware.api.BatteryManager
import com.mp3cool.hardware.api.EpaperManager
import com.mp3cool.hardware.api.HapticManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var epaperManager: EpaperManager
    @Inject lateinit var batteryManager: BatteryManager
    @Inject lateinit var audioRouteManager: AudioRouteManager
    @Inject lateinit var hapticManager: HapticManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mp3CoolTheme {
                PlayerApp(
                    epaperManager = epaperManager,
                    batteryManager = batteryManager,
                    audioRouteManager = audioRouteManager,
                    hapticManager = hapticManager,
                )
            }
        }
    }
}
