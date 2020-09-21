package com.example.examen.utils

import android.content.Context
import androidx.annotation.StringRes
import kotlin.Throwable

/**
 * Created by Sherzodbek Muhammadiev on 20.01.2020
 */

sealed class MessageData(val level: Level) {

    internal class Resource internal constructor(@StringRes val res: Int, level: Level) : MessageData(level)
    internal class Message internal constructor(val message: String, level: Level) : MessageData(level)
    internal class Failure internal constructor(val exception: Throwable, level: Level) : MessageData(level)

    fun isMessage() = this is Message
    fun isResource() = this is Resource
    fun isFailure() = this is Failure

    fun getMessageOrNull(): String? = (this as? Message)?.message
    fun getResourceOrNull(): Int? = (this as? Resource)?.res
    fun getFailureOrNull(): Throwable? = (this as? Failure)?.exception

    inline fun onMessage(f: (String) -> Unit): MessageData {
        if (isMessage()) getMessageOrNull()?.let { f(it) }
        return this
    }

    inline fun onResource(f: (Int) -> Unit): MessageData {
        if (isResource()) getResourceOrNull()?.let { f(it) }
        return this
    }

    inline fun onFailure(f: (Throwable) -> Unit): MessageData {
        if (isFailure()) getFailureOrNull()?.let { f(it) }
        return this
    }

    inline fun onResult(f: (Int?, String?, Throwable?, Any?) -> Unit): MessageData =
        onMessage { f(null, it, null, null) }.onResource { f(it, null, null, null) }.onFailure { f(null, null, it, null) }

    inline fun onResultMessage(context: Context, f: (String) -> Unit): MessageData =
        onMessage { f(it) }.onResource { f(context.getString(it)) }.onFailure { f(it.message.toString()) }

    enum class Level {
        DIALOG, TOAST, MAYBE_IGNORE, IGNORE, INPUT
    }

    companion object {
        fun message(message: String, level: Level = Level.MAYBE_IGNORE): MessageData = Message(message, level)
        fun resource(@StringRes res: Int, level: Level = Level.MAYBE_IGNORE): MessageData = Resource(res, level)
        fun failure(exception: Throwable, level: Level = Level.MAYBE_IGNORE): MessageData = Failure(exception, level)
    }
}