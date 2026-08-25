package com.mp3cool.hardware.mock

import com.mp3cool.hardware.api.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {
    @Binds @Singleton abstract fun epaper(impl: MockEpaperManager): EpaperManager
    @Binds @Singleton abstract fun haptics(impl: MockHapticManager): HapticManager
    @Binds @Singleton abstract fun controls(impl: MockPhysicalControlsManager): PhysicalControlsManager
    @Binds @Singleton abstract fun battery(impl: MockBatteryManager): BatteryManager
    @Binds @Singleton abstract fun audio(impl: MockAudioRouteManager): AudioRouteManager
    @Binds @Singleton abstract fun updates(impl: FakeUpdateManager): UpdateManager

    companion object {
        @Provides
        @Singleton
        fun epaperTiming(): EpaperMockTiming = EpaperMockTiming()
    }
}
