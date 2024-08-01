package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.components.Dropdown
import com.germanautolabs.acaraus.screens.components.IntervalDatePickerScreen
import java.time.LocalDate

data class ArticleFilterState(
    val filter: ArticleFilter = ArticleFilter(),
    val isVisible: Boolean = false,
    val isSet: Boolean = false,
    val setQuery: (String) -> Unit = {},
    val setSortBy: (SortBy) -> Unit = {},
    val setSource: (ArticleSource) -> Unit = {},
    val setLanguage: (String) -> Unit = {},
    val setFromDate: (LocalDate) -> Unit = {},
    val setToDate: (LocalDate) -> Unit = {},
    val show: () -> Unit = {},
    val apply: () -> Unit = {},
    val reset: () -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleFilter(
    modifier: Modifier = Modifier,
    filterState: ArticleFilterState,
    initialValue: SheetValue = SheetValue.Hidden,
) {
    if (filterState.isVisible.not()) return

    ModalBottomSheet(
        modifier = modifier,
        sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = LocalDensity.current,
            initialValue = initialValue,
        ),
        onDismissRequest = filterState.show,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = filterState.filter.query,
                onValueChange = filterState.setQuery,
                placeholder = { Text("Search topics") },
                trailingIcon = {
                    IconButton(onClick = { filterState.setQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
            )
            Dropdown(
                modifier = Modifier.padding(top = 16.dp),
                label = "Sort by",
                selection = "Most recent",
                options = setOf("Most recent", "Relevancy", "Popularity"),
            ) { }
            Dropdown(
                modifier = Modifier.padding(top = 16.dp),
                label = "Sources",
                selection = "All",
                options = setOf("All", "Source 1", "Source 2"),
            ) { }
            Dropdown(
                modifier = Modifier.padding(top = 16.dp),
                label = "Language",
                selection = "All",
                options = setOf("English", "French", "German"),
            ) { }
            IntervalDatePickerScreen(
                modifier = Modifier.padding(top = 16.dp),
                from = filterState.filter.fromDate,
                to = filterState.filter.toDate,
                onFromChange = filterState.setFromDate,
                onToChange = filterState.setToDate,
            )
            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = filterState.reset,
                ) { Text("Reset") }
                Button(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = filterState.apply,
                ) { Text("Apply") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun ArticleFilterPreview() {
    ArticleFilter(filterState = ArticleFilterState().copy(isVisible = true), initialValue = SheetValue.Expanded)
}
