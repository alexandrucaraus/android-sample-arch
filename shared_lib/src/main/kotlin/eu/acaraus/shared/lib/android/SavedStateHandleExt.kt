package eu.acaraus.shared.lib.android

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.net.URLDecoder

inline fun <reified T> SavedStateHandle.getValue(key: String, defaultValue: T): T =
    Json.decodeFromString<T>(URLDecoder.decode(this.get<String>(key), "UTF-8")) ?: run {
        this[key] = defaultValue
        defaultValue
    }

inline fun <reified T> SavedStateHandle.asStateFlow(key: String, defaultValue: T): StateFlow<T> =
    MutableStateFlow(getValue(key, defaultValue)).asStateFlow()
