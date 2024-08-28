package com.germanautolabs.acaraus.screens.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun RequestPermission(
    requestedPermission: String = android.Manifest.permission.RECORD_AUDIO,
    onPermissionGranted: (Boolean) -> Unit = {},
) {
    val activity = LocalContext.current as? Activity ?: return

    val permissionsRequester = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            println("Permission granted $granted")
            onPermissionGranted(granted)
        },
    )

    var launchRequest by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }

    when (activity.checkPermissionState(requestedPermission)) {
        PermissionState.Granted -> onPermissionGranted(true)
        PermissionState.DeniedPermanently -> {
            showRationale = true
        }

        else -> {
            launchRequest = true
        }
    }

    RationaleDialog(show = showRationale, hide = {
        onPermissionGranted(false)
        showRationale = false
    })

    LaunchedEffect(key1 = launchRequest) {
        if (launchRequest) {
            permissionsRequester.launch(requestedPermission)
        }
    }
}

private sealed class PermissionState {
    data object Granted : PermissionState()
    data object Denied : PermissionState()
    data object DeniedPermanently : PermissionState()
}

private fun Activity.checkPermissionState(permission: String): PermissionState {
    return when {
        hasPermissionGranted(permission) -> PermissionState.Granted
        hasPermissionDenied(permission) -> PermissionState.DeniedPermanently
        else -> PermissionState.Denied
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
    hide: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    if (show) {
        AlertDialog(
            onDismissRequest = {
                // Called when the user tries to dismiss the dialog by clicking outside or pressing the back button
                hide()
            },
            confirmButton = {
                TextButton(onClick = {
                    // Confirm button action
                    activity.openAppSettings()
                    hide()
                }) {
                    Text("Change in settings")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Dismiss button action
                    hide()
                }) {
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
