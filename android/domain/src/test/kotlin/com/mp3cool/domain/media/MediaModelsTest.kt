package com.mp3cool.domain.media

import org.junit.Assert.assertThrows
import org.junit.Test

class MediaModelsTest {
    @Test
    fun trackIdentityCannotBeBlank() {
        assertThrows(IllegalArgumentException::class.java) { TrackId(" ") }
    }

    @Test
    fun durationCannotBeNegative() {
        assertThrows(IllegalArgumentException::class.java) {
            Track(TrackId("track"), "Title", "Artist", "Album", durationMs = -1)
        }
    }
}
