package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun AudioCommandButtonPreview() = Column {
    AudioCommandButton(
        state = AudioCommandButtonState(
            hasSpeechRecognition = true,
            isListening = false,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            hasSpeechRecognition = true,
            isListening = true,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            hasSpeechRecognition = true,
            isListening = true,
            audioInputChangesDb = 300f,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            hasSpeechRecognition = true,
            isListening = true,
            audioInputChangesDb = 0f,
        ),
    )
}
