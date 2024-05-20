package dev.wiskiw.spacexapp.app.logger

interface AppLogger {
    fun logError(
        message: String,
        exception: Exception? = null,
    )
}
