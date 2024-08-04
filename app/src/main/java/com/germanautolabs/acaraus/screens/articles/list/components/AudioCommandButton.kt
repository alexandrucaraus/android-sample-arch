package com.germanautolabs.acaraus.screens.articles.list.components

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.germanautolabs.acaraus.screens.components.RequestRecordAudioPermission

data class AudioCommandState(
    val hasSpeechRecognition: Boolean = false,
    val isListening: Boolean = false,
    val audioInputChangesDb: Float = 0f,
    val toggleListening: () -> Unit = {},
)

@Composable
fun AudioCommandButton(
    modifier: Modifier = Modifier,
    state: AudioCommandState,
) {
    if (state.hasSpeechRecognition.not()) return
    val context = LocalContext.current
    val iconTint = if (state.isListening) Color.Red else LocalContentColor.current
    var requestPermission by remember { mutableStateOf(false) }
    FloatingActionButton(onClick = {
        if (hasPermission(context)) {
            state.toggleListening()
        } else {
            requestPermission = true
        }
    }) {
        if (state.isListening) {
            val scaleDB = state.audioInputChangesDb.coerceIn(
                minimumValue = 1f,
                maximumValue = 2f,
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

    if (requestPermission) {
        RequestRecordAudioPermission()
    }
}

private fun hasPermission(context: Context) = ContextCompat.checkSelfPermission(
    context, android.Manifest.permission.RECORD_AUDIO,
) == android.content.pm.PackageManager.PERMISSION_GRANTED
