package com.mp3cool.hardware.mock

import com.mp3cool.hardware.api.EpaperContent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MockEpaperManagerTest {
    @Test
    fun showAndSleepExposeStableState() = runBlocking {
        val manager = MockEpaperManager(EpaperMockTiming(partialRefreshMs = 0, fullRefreshMs = 0))
        val content = EpaperContent.Player("Track", "Artist", isPlaying = true)

        manager.show(content)
        manager.sleep()

        assertEquals(content, manager.state.value.content)
        assertEquals(1, manager.state.value.refreshCount)
        assertFalse(manager.state.value.awake)
    }
}
