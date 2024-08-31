package eu.acaraus.design.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

data class ToasterState(
    val showToast: Boolean = false,
    val message: String = "",
    val resetToast: () -> Unit = {},
)

@Composable
fun Toaster(
    state: ToasterState,
) {
    val context = LocalContext.current
    LaunchedEffect(state.showToast) {
        if (state.showToast && state.message.isNotEmpty()) {
            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            state.resetToast()
        }
    }
}
