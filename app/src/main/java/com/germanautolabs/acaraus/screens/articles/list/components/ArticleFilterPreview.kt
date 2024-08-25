package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ArticleFilterPreview() {
    ArticleFilter(
        filterState = ArticlesFilterEditorState().copy(isVisible = true),
        sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = LocalDensity.current,
            initialValue = SheetValue.Expanded,
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun ArticleFilterActionIconPreview() = Column {
    // Filter not set
    ArticleFilterActionIcon(
        modifier = Modifier,
        filterState = ArticlesFilterEditorState(),
    )
    // Filter set
    ArticleFilterActionIcon(
        modifier = Modifier,
        filterState = ArticlesFilterEditorState(
            query = "dallas",
        ),
    )
}
