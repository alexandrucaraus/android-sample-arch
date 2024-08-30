package eu.acaraus.news.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.DecorationBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    label: String,
    selection: String,
    options: Set<String>,
    onSelect: (String) -> Unit,
) {
    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable {
                setExpanded(!isExpanded)
                focusRequester.requestFocus()
            }
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .focusTarget(),
    ) {
        DecorationBox(
            value = selection,
            innerTextField = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = selection,
                )
            },
            label = { Text(label) },
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = OutlinedTextFieldDefaults.colors(),
        )
    }

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { setExpanded(false) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        options.forEach { option ->
            DropdownMenuItem(text = {
                Text(text = option)
            }, onClick = {
                onSelect(option)
                setExpanded(false)
            })
        }
    }
}
