package com.mp3cool.hardware.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class HardwareModelsTest {
    @Test
    fun rejectsInvalidEpaperRegions() {
        assertThrows(IllegalArgumentException::class.java) {
            EpaperRegion(x = 0, y = 0, width = 0, height = 20)
        }
    }

    @Test
    fun acceptsBoundaryBatteryPercentages() {
        assertEquals(0, DeviceBatteryState(0, charging = false).percentage)
        assertEquals(100, DeviceBatteryState(100, charging = true).percentage)
    }

    @Test
    fun rejectsOutOfRangeVolume() {
        assertThrows(IllegalArgumentException::class.java) { EpaperContent.Volume(101) }
    }
}
