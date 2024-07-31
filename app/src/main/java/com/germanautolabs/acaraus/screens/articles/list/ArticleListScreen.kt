package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.dummyArticleList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    listState: ArticleListState,
    filterState: ArticleFilterState,
    onNavigateToDetails: (article: Article) -> Unit,
) = Column(modifier = modifier) {
    TopAppBar(
        title = { Text("News") },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Mic, null)
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.FilterList, null)
            }
        },
    )
    when {
        listState.isLoading -> ArticleListLoading()
        listState.isError -> ArticleListError(errorMessage = listState.errorMessage!!)
        else -> ArticleList(articles = listState.list, onNavigateToDetails = onNavigateToDetails)
    }
}

@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onNavigateToDetails: (article: Article) -> Unit,
) = LazyColumn(
    modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
) {
    items(articles) { article ->
        ArticleListItem(
            article = article,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}

@Composable
fun ArticleListItem(
    modifier: Modifier = Modifier,
    article: Article,
    onNavigateToDetails: (article: Article) -> Unit,
) = Card(
    modifier = modifier.fillMaxWidth(),
    onClick = { onNavigateToDetails(article) },
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text("Title: ${article.title}")
        Text("Description: ${article.description}")
        Text("Source: ${article.source}")
    }
}

@Composable
fun ArticleListLoading(
    modifier: Modifier = Modifier,
) = Card(modifier = modifier) {
    Text("Loading...")
}

@Composable
fun ArticleListError(
    modifier: Modifier = Modifier,
    errorMessage: String,
) = Card(modifier = modifier) {
    Text("Ups there is an error: $errorMessage")
}

@Composable
@Preview
fun ArticleListStateSuccessPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        listState = ArticleListState(list = dummyArticleList, isError = false, errorMessage = null, load = {}, toggleListening = {}),
        filterState = ArticleFilterState(filter = ArticleFilter(), setQuery = {}),
        onNavigateToDetails = {},
    )
}

@Composable
@Preview
fun ArticleListStateLoadingPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        listState = ArticleListState(list = emptyList(), isLoading = true, isError = false, errorMessage = "null", load = {}, toggleListening = {}),
        filterState = ArticleFilterState(filter = ArticleFilter(), setQuery = {}),
        onNavigateToDetails = {},
    )
}

@Composable
@Preview
fun ArticleListStateErrorPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        listState = ArticleListState(list = emptyList(), isError = true, errorMessage = "null", load = {}, toggleListening = {}),
        filterState = ArticleFilterState(filter = ArticleFilter(), setQuery = {}),
        onNavigateToDetails = {},
    )
}
