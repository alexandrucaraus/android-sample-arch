package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    vm: ArticleDetailViewModel,
    onNavigateBack: () -> Unit,
) = Column(modifier = modifier.padding(all = 16.dp)) {

    Button(onClick = onNavigateBack) {
        Text("Close")
    }
    Text("Article Detail Screen article: ${vm.articleId}")
}
