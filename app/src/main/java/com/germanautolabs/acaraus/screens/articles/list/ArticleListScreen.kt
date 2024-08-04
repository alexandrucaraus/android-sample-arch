package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilter
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterActionIcon
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleList
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandButton
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandState
import com.germanautolabs.acaraus.screens.components.Toaster
import com.germanautolabs.acaraus.screens.components.ToasterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    articleListState: ArticleListState,
    articleFilterState: ArticleFilterState,
    audioCommandState: AudioCommandState,
    toasterState: ToasterState,
    onNavigateToDetails: (article: Article) -> Unit,
) = Scaffold(modifier = modifier, topBar = {
    TopAppBar(
        title = { Text("News") },
        actions = { ArticleFilterActionIcon(filterState = articleFilterState) },
    )
}, floatingActionButton = {
    AudioCommandButton(state = audioCommandState)
}, content = { padding ->
    Column {
        ArticleList(
            modifier = Modifier.padding(padding),
            listState = articleListState,
            onNavigateToDetails = onNavigateToDetails,
        )

        ArticleFilter(filterState = articleFilterState)

        Toaster(state = toasterState)
    }
})

@Composable
@Preview
fun ArticleListStateLoadingPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(isLoading = true),
        articleFilterState = ArticleFilterState(),
        toasterState = ToasterState(),
        audioCommandState = AudioCommandState(),
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
        toasterState = ToasterState(),
        audioCommandState = AudioCommandState(),
        onNavigateToDetails = {},
    )
}
