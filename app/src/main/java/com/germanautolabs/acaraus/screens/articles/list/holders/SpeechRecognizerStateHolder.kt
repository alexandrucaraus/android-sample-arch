package com.germanautolabs.acaraus.screens.articles.list.holders

import com.germanautolabs.acaraus.data.SpeechEvent
import com.germanautolabs.acaraus.data.SpeechRecognizer
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandButtonState
import com.germanautolabs.acaraus.screens.components.ToasterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
class SpeechRecognizerStateHolder(
    private val speechRecognizer: SpeechRecognizer,
    @InjectedParam scope: CoroutineScope,
) : CoroutineScope by scope {

    private val audioCommandButtonState = MutableStateFlow(
        AudioCommandButtonState(
            hasSpeechRecognition = speechRecognizer.isAvailable.value,
            toggleListening = ::toggleListening,
        ),
    )

    private val toasterState = MutableStateFlow(
        ToasterState(
            resetToast = ::resetToast,
        ),
    )

    val audioCommandButton = audioCommandButtonState.asStateFlow()
    val toaster = toasterState.asStateFlow()

    init {

        speechRecognizer.isListening.onEach { isListening ->
            audioCommandButtonState.update { it.copy(isListening = isListening) }
        }.launchIn(this)

        speechRecognizer.events().onEach { event ->
            when (event) {
                is SpeechEvent.Result -> reloadVoiceCommand(event.matches)
                is SpeechEvent.Error -> showToast(event.errorMessage)
                is SpeechEvent.RmsChanged -> audioCommandButtonState.update { it.copy(audioInputChangesDb = event.rmsdB) }
                else -> {
                    /* no op */
                }
            }
        }.launchIn(this)

        coroutineContext.job.invokeOnCompletion { speechRecognizer.destroy() }
    }

    private fun toggleListening() {
        if (speechRecognizer.isListening.value) {
            speechRecognizer.stopListening()
        } else {
            speechRecognizer.startListening()
        }
    }

    //
    private fun reloadVoiceCommand(spokenWords: List<String>) {
        spokenWords.find { it.contains("Reload", ignoreCase = true) }?.let {
            showToast("Received reloading command...")
            // todo reload articles
            // reloadArticles()
        } ?: {
            showToast("Command not recognized")
        }
    }

    private fun showToast(message: String) {
        toasterState.update { it.copy(showToast = true, message = message) }
    }

    private fun resetToast() {
        toasterState.update { it.copy(showToast = false, message = "") }
    }

}
