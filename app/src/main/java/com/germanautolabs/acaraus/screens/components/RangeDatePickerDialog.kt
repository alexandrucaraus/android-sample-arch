package com.germanautolabs.acaraus.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeDatePickerDialog(
    modifier: Modifier = Modifier,
    oldestDate: LocalDate,
    newestDate: LocalDate,
    onRangeSelected: (old: LocalDate, new: LocalDate) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }
    val dateRangePicker = rememberDateRangePickerState(
        initialSelectedStartDateMillis = oldestDate.toEpochMillis(),
        initialSelectedEndDateMillis = newestDate.toEpochMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= LocalDate.now().plusDays(1).toEpochMillis()
            }
        },
    )

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { showPicker = true },
    ) {
        Text(oldestDate.format() + " - " + newestDate.format())
    }

    if (showPicker) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showPicker = false },
            content = {
                DateRangePicker(
                    modifier = Modifier.height(height = 500.dp),
                    state = dateRangePicker,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = {
                        showPicker = false
                    }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        showPicker = false
                        val old = dateRangePicker.selectedStartDateMillis
                        val new = dateRangePicker.selectedEndDateMillis
                        dateRangePicker.setSelection(old, new)
                        onRangeSelected(old.epochMillisToLocalDate(), new.epochMillisToLocalDate())
                    }) {
                        Text("Apply")
                    }
                }
            },
        )
    }
}

private fun Long?.epochMillisToLocalDate(): LocalDate =
    if (this == null) LocalDate.now() else LocalDate.ofEpochDay(this / 1000 / 60 / 60 / 24)

private fun LocalDate.toEpochMillis(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun LocalDate.format(): String =
    DateTimeFormatter.ofPattern("MMM dd, yyyy").format(this)
