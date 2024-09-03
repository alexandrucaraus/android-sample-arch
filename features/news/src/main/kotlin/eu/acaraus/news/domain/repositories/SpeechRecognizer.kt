package eu.acaraus.news.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

sealed class SpeechEvent {
    data class RmsChanged(val rmsdB: Float) : SpeechEvent()
    data class Error(val errorMessage: String) : SpeechEvent()
    data class Result(val matches: List<String>) : SpeechEvent()
}

interface SpeechRecognizer {
    val isListening: MutableStateFlow<Boolean>
    val isAvailable: MutableStateFlow<Boolean>
    fun events(): Flow<SpeechEvent>
    fun startListening()
    fun stopListening()
    fun toggleListening()
    fun destroy()
}
