package com.germanautolabs.acaraus.screens.articles.list.holders

import com.germanautolabs.acaraus.data.SpeechEvent
import com.germanautolabs.acaraus.data.SpeechRecognizer
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandButtonState
import com.germanautolabs.acaraus.screens.components.PermissionState
import com.germanautolabs.acaraus.screens.components.ToasterState
import com.germanautolabs.acaraus.usecase.MatchVoiceCommands
import com.germanautolabs.acaraus.usecase.VoiceCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Scope

@Factory
@Scope(ArticlesListKoinScope::class)
class SpeechRecognizerStateHolder(
    private val speechRecognizer: SpeechRecognizer,
    private val matchVoiceCommands: MatchVoiceCommands,
    scope: CoroutineScope,
) : CoroutineScope by scope {

    private val audioCommandButtonState = MutableStateFlow(
        AudioCommandButtonState(
            isEnabled = speechRecognizer.isAvailable.value,
            toggleListening = ::toggleListening,
            changePermissionState = ::changePermissionsState,
        ),
    )

    private val toasterState = MutableStateFlow(
        ToasterState(resetToast = ::resetToast),
    )

    val audioCommandButtonUi = audioCommandButtonState.asStateFlow()
    val toasterUi = toasterState.asStateFlow()

    val reloadCommand = MutableSharedFlow<Unit>()

    init {
        speechRecognizer.isListening.onEach(::updateListeningState).launchIn(this)

        speechRecognizer.events().onEach { event ->
            when (event) {
                is SpeechEvent.Result -> executeVoiceCommands(event.matches)
                is SpeechEvent.Error -> showToast(event.errorMessage)
                is SpeechEvent.RmsChanged -> updateAudioInputLevel(event.rmsdB)
            }
        }.launchIn(this)

        coroutineContext.job.invokeOnCompletion { speechRecognizer.destroy() }
    }

    private suspend fun executeVoiceCommands(words: List<String>) {
        when (matchVoiceCommands(words)) {
            is VoiceCommand.Reload -> reloadCommand.emit(Unit)
            is VoiceCommand.Unknown -> showToast("Command not recognized")
        }
    }

    private fun toggleListening() {
        updatePermissionStateTo(PermissionState.ConfirmationRequested)
    }

    private fun changePermissionsState(newPermissionState: PermissionState) {
        if (newPermissionState.isEqualTo(PermissionState.Granted)) {
            speechRecognizer.toggleListening()
            updatePermissionStateTo(PermissionState.ConfirmationNeeded)
        } else {
            updatePermissionStateTo(newPermissionState)
        }
    }

    private fun updateListeningState(isListening: Boolean) {
        audioCommandButtonState.update { it.copy(isListening = isListening) }
    }

    private fun updateAudioInputLevel(inputLevel: Float) {
        audioCommandButtonState.update { it.copy(audioInputLevel = inputLevel) }
    }

    private fun updatePermissionStateTo(permissionState: PermissionState) {
        audioCommandButtonState.update { it.copy(permissionState = permissionState) }
    }

    private fun showToast(message: String) {
        toasterState.update { it.copy(showToast = true, message = message) }
    }

    private fun resetToast() {
        toasterState.update { it.copy(showToast = false, message = "") }
    }
}
