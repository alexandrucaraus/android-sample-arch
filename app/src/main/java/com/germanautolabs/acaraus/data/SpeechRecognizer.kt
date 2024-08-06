package com.germanautolabs.acaraus.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import androidx.annotation.MainThread
import com.germanautolabs.acaraus.infra.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory
import android.speech.SpeechRecognizer as AndroidSpeechRecognizer

sealed class SpeechEvent {
    data object ReadyForSpeech : SpeechEvent()
    data object BeginOfSpeech : SpeechEvent()
    data object EndOfSpeech : SpeechEvent()
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
    fun destroy()
}

@Factory(binds = [SpeechRecognizer::class])
class SpeechRecognizerImpl(
    context: Context,
    private val dispatchers: Dispatchers,
) : SpeechRecognizer {

    override val isAvailable = MutableStateFlow(
        AndroidSpeechRecognizer.isRecognitionAvailable(context),
    )

    override val isListening = MutableStateFlow(false)

    private val androidRecognizer: AndroidSpeechRecognizer =
        AndroidSpeechRecognizer.createSpeechRecognizer(context)

    override fun startListening() {
        androidRecognizer.startListening(startParams())
    }


    override fun stopListening() {
        androidRecognizer.stopListening()
    }

    override fun destroy() {
        androidRecognizer.destroy()
    }

    override fun events(): Flow<SpeechEvent> = callbackFlow {
        androidRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                trySend(SpeechEvent.ReadyForSpeech)
            }

            override fun onBeginningOfSpeech() {
                isListening.value = true
                trySend(SpeechEvent.BeginOfSpeech)
            }

            override fun onEndOfSpeech() {
                trySend(SpeechEvent.EndOfSpeech)
            }

            override fun onResults(results: Bundle?) {
                val matches =
                    results?.getStringArrayList(AndroidSpeechRecognizer.RESULTS_RECOGNITION)
                        ?.toList().orEmpty()
                trySend(SpeechEvent.Result(matches))
                isListening.value = false
            }

            override fun onError(error: Int) {
                trySend(SpeechEvent.Error(getSpeechRecognizerErrorString(error)))
                isListening.value = false
            }

            override fun onRmsChanged(rmsdB: Float) {
                trySend(SpeechEvent.RmsChanged(rmsdB))
            }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        awaitClose { /* no op */ }
    }

    fun getSpeechRecognizerErrorString(errorCode: Int): String {
        return when (errorCode) {
            AndroidSpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            AndroidSpeechRecognizer.ERROR_CLIENT -> "Client side error"
            AndroidSpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            AndroidSpeechRecognizer.ERROR_NETWORK -> "Network error"
            AndroidSpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            AndroidSpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            AndroidSpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            AndroidSpeechRecognizer.ERROR_SERVER -> "Server error"
            AndroidSpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
    }

    private fun startParams(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        return intent
    }
}
