package com.germanautolabs.acaraus.usecase

import org.koin.core.annotation.Factory

sealed class VoiceCommand {
    data object Reload : VoiceCommand()
    data object Unknown : VoiceCommand()
}

@Factory
class MatchVoiceCommands {
    operator fun invoke(spokenWords: List<String>): VoiceCommand =
        spokenWords.find { it.contains("Reload", ignoreCase = true) }?.let {
            return VoiceCommand.Reload
        } ?: run {
            return VoiceCommand.Unknown
        }
}
