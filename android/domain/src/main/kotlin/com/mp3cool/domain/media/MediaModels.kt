package com.mp3cool.domain.media

/** Hardware- and Android-independent identity used across feature boundaries. */
@JvmInline
value class TrackId(val value: String) {
    init {
        require(value.isNotBlank()) { "TrackId cannot be blank" }
    }
}

data class Track(
    val id: TrackId,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val artworkUri: String? = null,
) {
    init {
        require(durationMs >= 0) { "Track duration cannot be negative" }
    }
}

enum class RepeatMode { OFF, ONE, ALL }
