package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    vm: ArticleDetailViewModel,
    onNavigateBack: () -> Unit,
) = Column(modifier = modifier) {
    Text("Article Detail Screen article: ${vm.articleId}")
    Button(onClick = onNavigateBack) {
        Text("Close")
    }
}
