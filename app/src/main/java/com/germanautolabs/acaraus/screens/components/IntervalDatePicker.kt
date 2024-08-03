package com.germanautolabs.acaraus.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalDatePickerScreen(
    modifier: Modifier = Modifier,
    fromLabel: String = "Oldest",
    from: LocalDate = LocalDate.now(),
    toLabel: String = "Newest",
    to: LocalDate = LocalDate.now().minusMonths(1),
    onFromChange: (LocalDate) -> Unit = {},
    onToChange: (LocalDate) -> Unit = {},
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(onClick = { showStartDatePicker = true }) {
            Text("$fromLabel: ${from.format()}")
        }
        Button(onClick = { showEndDatePicker = true }) {
            Text("$toLabel: ${to.format()}")
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = { showStartDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                onFromChange(datePickerState.selectedDateMillis.epochMillisToLocalDate())
                showStartDatePicker = false
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showStartDatePicker = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = { showEndDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                onToChange(datePickerState.selectedDateMillis.epochMillisToLocalDate())
                showEndDatePicker = false
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showEndDatePicker = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
@Preview
fun IntervalDatePickerScreenPreview() {
    IntervalDatePickerScreen()
}

private fun LocalDate?.format(): String =
    this?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "Not selected"

private fun Long?.epochMillisToLocalDate(): LocalDate =
    if (this == null) LocalDate.now() else LocalDate.ofEpochDay(this / 1000 / 60 / 60 / 24)
