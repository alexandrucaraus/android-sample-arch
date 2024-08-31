package eu.acaraus.design.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

sealed class PermissionState {
    data object ConfirmationNeeded : PermissionState()
    data object ConfirmationRequested : PermissionState()
    data object Granted : PermissionState()
    data object Denied : PermissionState()
    data object NotGranted : PermissionState()

    fun isEqualTo(state: PermissionState) = this == state
}

@Composable
fun RequestPermission(
    state: PermissionState = PermissionState.ConfirmationNeeded,
    changeState: (PermissionState) -> Unit,
    requestedPermission: String,
) {
    if (state.isEqualTo(PermissionState.ConfirmationNeeded)) return

    val activity = LocalContext.current as? Activity ?: return

    val permissionsRequester = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> changeState(activity.checkPermissionState(requestedPermission)) },
    )

    LaunchedEffect(key1 = state) {
        if (state is PermissionState.ConfirmationRequested) {
            permissionsRequester.launch(requestedPermission)
        }
    }

    RationaleDialog(
        show = state == PermissionState.NotGranted,
        openSettings = activity::openAppSettings,
        dismiss = { changeState(PermissionState.ConfirmationNeeded) },
    )
}

private fun Activity.checkPermissionState(permission: String): PermissionState {
    return when {
        hasPermissionGranted(permission) -> PermissionState.Granted
        hasPermissionDenied(permission) -> PermissionState.Denied
        else -> PermissionState.NotGranted
    }
}

private fun Activity.hasPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) ==
        android.content.pm.PackageManager.PERMISSION_GRANTED

private fun Activity.hasPermissionDenied(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

@Composable
fun RationaleDialog(
    show: Boolean,
    openSettings: () -> Unit,
    dismiss: () -> Unit,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = dismiss,
            confirmButton = {
                TextButton(onClick = {
                    openSettings()
                    dismiss()
                }) {
                    Text("Change in settings")
                }
            },
            dismissButton = {
                TextButton(onClick = dismiss) {
                    Text("Cancel")
                }
            },
            title = {
                Text(text = "Permission Required")
            },
            text = {
                Text("This permission is needed for this feature to work. Please allow it.")
            },
        )
    }
}

private fun Activity.openAppSettings() =
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        },
    )
