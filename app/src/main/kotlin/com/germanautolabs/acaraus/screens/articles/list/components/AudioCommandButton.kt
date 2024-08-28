package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.germanautolabs.acaraus.screens.components.RequestPermission

data class AudioCommandButtonState(
    val hasSpeechRecognition: Boolean = false,
    val hasRecordAudioPermission: Boolean = false,
    val isListening: Boolean = false,
    val audioInputChangesDb: Float = 0f,
    val setHasRecordAudioPermission: (Boolean) -> Unit = {},
    val toggleListening: () -> Unit = {},
)

@Composable
fun AudioCommandButton(
    modifier: Modifier = Modifier,
    state: AudioCommandButtonState,
) {
    if (state.hasSpeechRecognition.not()) return
    var requestPermission by remember { mutableStateOf(false) }

    AudioButton(
        modifier = modifier,
        state = state,
        onClick = {
            if (state.hasRecordAudioPermission) {
                state.toggleListening()
            } else {
                requestPermission = true
            }
        }
    )

    if (requestPermission) {
        RequestPermission(
            onPermissionGranted = { granted ->
                state.setHasRecordAudioPermission(granted)
                requestPermission = granted
            }
        )
    }
}

@Composable
private fun AudioButton(
    modifier: Modifier = Modifier,
    state: AudioCommandButtonState,
    onClick: () -> Unit,
) {
    val iconTint = if (state.isListening) Color.Red else LocalContentColor.current
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        if (state.isListening) {
            val scaleDB = state.audioInputChangesDb.coerceIn(
                minimumValue = 1f,
                maximumValue = 3f,
            )
            val iconSizeDp = (scaleDB * 20).coerceIn(
                minimumValue = 10f,
                maximumValue = 30f,
            ).dp
            Icon(
                modifier = Modifier.size(iconSizeDp),
                imageVector = Icons.Default.Mic,
                tint = iconTint,
                contentDescription = "Listen commands",
            )
        } else {
            Icon(Icons.Default.Mic, contentDescription = "Listen commands")
        }
    }
}
