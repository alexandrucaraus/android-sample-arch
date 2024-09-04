package eu.acaraus.news.presentation.list.holders

import eu.acaraus.design.components.PermissionState
import eu.acaraus.design.components.ToasterState
import eu.acaraus.news.domain.repositories.SpeechEvent
import eu.acaraus.news.domain.repositories.SpeechRecognizerService
import eu.acaraus.news.domain.usecases.MatchVoiceCommands
import eu.acaraus.news.domain.usecases.VoiceCommand
import eu.acaraus.news.presentation.list.components.AudioCommandButtonState
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
    private val speechRecognizerService: SpeechRecognizerService,
    private val matchVoiceCommands: MatchVoiceCommands,
    scope: CoroutineScope,
) : CoroutineScope by scope {

    private val audioCommandButtonState = MutableStateFlow(
        AudioCommandButtonState(
            isEnabled = speechRecognizerService.isAvailable.value,
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
        speechRecognizerService.isListening.onEach(::updateListeningState).launchIn(this)

        speechRecognizerService.events().onEach { event ->
            when (event) {
                is SpeechEvent.Result -> executeVoiceCommands(event.matches)
                is SpeechEvent.Error -> showToast(event.errorMessage)
                is SpeechEvent.RmsChanged -> updateAudioInputLevel(event.rmsdB)
            }
        }.launchIn(this)

        coroutineContext.job.invokeOnCompletion { speechRecognizerService.destroy() }
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
            speechRecognizerService.toggleListening()
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
