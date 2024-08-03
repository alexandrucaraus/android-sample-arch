package com.germanautolabs.acaraus.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.annotation.Factory
import android.speech.SpeechRecognizer as AndroidSpeechRecognizer

sealed class SpeechEvent {
    data object ReadyForSpeech : SpeechEvent()
    data object BeginOfSpeech : SpeechEvent()
    data class RmsChanged(val rms: Float) : SpeechEvent()
    data class BufferReceived(val buffer: ByteArray) : SpeechEvent()
    data object EndOfSpeech : SpeechEvent()
    data class Error(val code: Int) : SpeechEvent()
    data class PartialResult(val result: Bundle) : SpeechEvent()
    data class Result(val result: Bundle) : SpeechEvent()
    data class SubEvent(val subEvent: Int) : SpeechEvent()
}

interface SpeechRecognizer {
    fun startListening()
    fun stopListening()
    fun events(): Flow<SpeechEvent>
    val isListening: MutableStateFlow<Boolean>
}

@Factory(binds = [SpeechRecognizer::class])
class SpeechRecognizerImpl(
    private val context: Context,
) : SpeechRecognizer {

    private val androidRecognizer: AndroidSpeechRecognizer =
        AndroidSpeechRecognizer.createSpeechRecognizer(context)

    override fun startListening() {
        androidRecognizer.startListening(startParams())
    }

    override fun stopListening() {
        androidRecognizer.stopListening()
    }

    override val isListening = MutableStateFlow(false)

    override fun events(): Flow<SpeechEvent> = callbackFlow {
        androidRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening.value = true
                trySend(SpeechEvent.ReadyForSpeech)
            }
            override fun onBeginningOfSpeech() {
                trySend(SpeechEvent.BeginOfSpeech)
            }

            override fun onRmsChanged(rmsdB: Float) {
                trySend(SpeechEvent.RmsChanged(rmsdB))
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                buffer?.let { buf -> trySend(SpeechEvent.BufferReceived(buf)) }
            }

            override fun onEndOfSpeech() {
                isListening.value = false
                trySend(SpeechEvent.EndOfSpeech)
            }

            override fun onError(error: Int) {
                trySend(SpeechEvent.Error(error))
            }

            override fun onResults(results: Bundle?) {
                println("Results $results")
                results?.let { res -> trySend(SpeechEvent.Result(res)) }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                println("Partial results $partialResults")
                partialResults?.let { res -> trySend(SpeechEvent.PartialResult(res)) }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                trySend(SpeechEvent.SubEvent(eventType))
            }
        })

        awaitClose { /* hehe */ }
    }

    private fun startParams(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        // intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        return intent
    }
}
