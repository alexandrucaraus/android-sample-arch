package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun AudioCommandButtonPreview() = Column {
    AudioCommandButton(
        state = AudioCommandButtonState(
            isEnabled = true,
            isListening = false,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            isEnabled = true,
            isListening = true,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            isEnabled = true,
            isListening = true,
            audioInputLevel = 300f,
        ),
    )

    AudioCommandButton(
        state = AudioCommandButtonState(
            isEnabled = true,
            isListening = true,
            audioInputLevel = 0f,
        ),
    )
}
