package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.germanautolabs.acaraus.models.Article

@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    vm: ArticleListViewModel,
    onNavigateToDetails: (article: Article) -> Unit,
) = Column(modifier = modifier) {
    Text("Article List Screen")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(vm.list) { article ->
            ArticleListItem(
                article = article,
                onNavigateToDetails = onNavigateToDetails,
            )
        }
    }
}

@Composable
fun ArticleListItem(
    modifier: Modifier = Modifier,
    article: Article,
    onNavigateToDetails: (article: Article) -> Unit,
) = Card(onClick = { onNavigateToDetails(article) }) {
    Column(modifier = modifier.padding(8.dp)) {
        Text("Title: ${article.title}")
        Text("Description: ${article.description}")
        Text("Source: ${article.source}")
    }
}
