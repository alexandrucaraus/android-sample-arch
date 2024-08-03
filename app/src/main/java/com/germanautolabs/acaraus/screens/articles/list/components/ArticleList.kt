package com.germanautolabs.acaraus.screens.articles.list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.germanautolabs.acaraus.R
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.screens.articles.list.ArticleListScreen

data class ArticleListState(
    val list: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isListening: Boolean = false,
    val retry: () -> Unit = {},
    val toggleListening: () -> Unit = {},
)

@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    listState: ArticleListState,
    onNavigateToDetails: (article: Article) -> Unit,
) = Box(modifier = modifier.fillMaxSize()) {
    when {
        listState.isLoading -> ArticleListLoading(
            modifier = Modifier.align(Alignment.Center),
        )

        listState.isError -> ArticleListError(
            modifier = Modifier.align(Alignment.Center),
            retry = listState.retry,
            errorMessage = listState.errorMessage!!,
        )

        listState.list.isEmpty() ->
            ArticleListEmpty(modifier = Modifier.align(Alignment.Center))

        else -> ArticleListSuccess(
            articles = listState.list,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}

@Composable
fun ArticleListSuccess(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onNavigateToDetails: (article: Article) -> Unit,
) = LazyColumn(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
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
    var expanded by remember { mutableStateOf(false) }
    if (article.imageURL.isNullOrEmpty().not()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentDescription = article.description,
            model = article.imageURL,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Crop,
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = article.title,
            )
            Text(style = MaterialTheme.typography.labelMedium, text = article.source)
        }
        if (article.description.isNullOrEmpty().not()) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.UnfoldLess else Icons.Default.UnfoldMore,
                    null,
                )
            }
        }
    }
    AnimatedVisibility(
        visible = expanded,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
    ) {
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = article.description ?: "",
        )
    }
}

@Composable
fun ArticleListLoading(
    modifier: Modifier = Modifier,
) = Card(modifier = modifier) {
    CircularProgressIndicator(
        modifier = Modifier.size(60.dp),
    )
}

@Composable
fun ArticleListEmpty(
    modifier: Modifier = Modifier,
) = Card(modifier = modifier) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No results, change filter settings")
    }
}

@Composable
fun ArticleListError(
    modifier: Modifier = Modifier,
    retry: () -> Unit,
    errorMessage: String,
) = Card(modifier = modifier) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Error: $errorMessage")
        Button(onClick = retry) {
            Text("Retry")
        }
    }
}

@Composable
@Preview
fun ArticleListStateLoadingPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(isLoading = true),
        articleFilterState = ArticleFilterState(),
        onNavigateToDetails = {},
    )
}

@Composable
@Preview
fun ArticleListStateErrorPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(isError = true, errorMessage = "null"),
        articleFilterState = ArticleFilterState(),
        onNavigateToDetails = {},
    )
}
