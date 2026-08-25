package com.mp3cool.core.logging

import android.util.Log

enum class LogCategory {
    PLAYBACK, LIBRARY, SPOTIFY, EPAPER, HARDWARE, BLUETOOTH, WIFI, POWER, UPDATE, DATABASE
}

interface PlayerLogger {
    fun debug(category: LogCategory, message: String)
    fun warning(category: LogCategory, message: String, error: Throwable? = null)
    fun error(category: LogCategory, message: String, error: Throwable? = null)
}

/** Development logger. Production diagnostic storage can replace this at the DI boundary. */
class AndroidPlayerLogger : PlayerLogger {
    override fun debug(category: LogCategory, message: String) {
        Log.d(category.tag, message)
    }

    override fun warning(category: LogCategory, message: String, error: Throwable?) {
        Log.w(category.tag, message, error)
    }

    override fun error(category: LogCategory, message: String, error: Throwable?) {
        Log.e(category.tag, message, error)
    }

    private val LogCategory.tag: String get() = "MP3cool/$name"
}
