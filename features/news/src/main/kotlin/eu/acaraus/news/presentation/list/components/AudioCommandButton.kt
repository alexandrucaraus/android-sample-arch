package eu.acaraus.news.presentation.list.components

import android.Manifest
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.acaraus.design.components.PermissionState
import eu.acaraus.design.components.RequestPermission

data class AudioCommandButtonState(
    val isEnabled: Boolean = false,
    val isListening: Boolean = false,
    val audioInputLevel: Float = 0f,
    val permissionState: PermissionState = PermissionState.ConfirmationNeeded,
    val changePermissionState: (PermissionState) -> Unit = {},
    val toggleListening: () -> Unit = {},
)

@Composable
fun AudioCommandButton(
    modifier: Modifier = Modifier,
    state: AudioCommandButtonState,
) {
    if (state.isEnabled.not()) return

    AudioButton(
        modifier = modifier,
        state = state,
        onClick = state.toggleListening,
    )

    RequestPermission(
        state = state.permissionState,
        changeState = state.changePermissionState,
        requestedPermission = Manifest.permission.RECORD_AUDIO,
    )
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
            val scaleDB = state.audioInputLevel.coerceIn(
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
