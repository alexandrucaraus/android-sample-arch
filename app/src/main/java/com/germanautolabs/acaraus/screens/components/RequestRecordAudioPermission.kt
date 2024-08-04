package com.germanautolabs.acaraus.screens.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun RequestRecordAudioPermission() {
    val permission = android.Manifest.permission.RECORD_AUDIO
    val permissions =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { _ -> },
        )
    LaunchedEffect(key1 = Unit) {
        permissions.launch(permission)
    }
}
