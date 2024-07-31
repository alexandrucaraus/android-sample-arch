package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    vm: ArticleDetailViewModel,
    onNavigateBack: () -> Unit,
) = Column(modifier = modifier) {
    TopAppBar(
        title = {
            Text("Article Detail Screen article: ${vm.articleId}")
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.testTag("Close"),
                onClick = { onNavigateBack() }) {
                Icon(imageVector = Icons.Default.Close, null)
            }
        },
    )
}

@Composable
@Preview
fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(
        vm = ArticleDetailViewModel("0"),
        onNavigateBack = {},
    )
}
