package eu.acaraus.news.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

sealed class SpeechEvent {
    data class RmsChanged(val rmsdB: Float) : SpeechEvent()
    data class Error(val errorMessage: String) : SpeechEvent()
    data class Result(val matches: List<String>) : SpeechEvent()
}

interface SpeechRecognitionService {
    val isListening: MutableStateFlow<Boolean>
    val isAvailable: MutableStateFlow<Boolean>
    fun events(): Flow<SpeechEvent>
    fun startListening()
    fun stopListening()
    fun toggleListening()
    fun destroy()
}
