package eu.acaraus.news.presentation.articles.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.presentation.articles.list.components.ArticleFilter
import eu.acaraus.news.presentation.articles.list.components.ArticleFilterActionIcon
import eu.acaraus.news.presentation.articles.list.components.ArticleList
import eu.acaraus.news.presentation.articles.list.components.ArticleListState
import eu.acaraus.news.presentation.articles.list.components.ArticlesFilterEditorState
import eu.acaraus.news.presentation.articles.list.components.AudioCommandButton
import eu.acaraus.news.presentation.articles.list.components.AudioCommandButtonState
import eu.acaraus.news.presentation.components.Toaster
import eu.acaraus.news.presentation.components.ToasterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    articleListState: ArticleListState,
    articlesFilterEditorState: ArticlesFilterEditorState,
    audioCommandButtonState: AudioCommandButtonState,
    toasterState: ToasterState,
    onNavigateToDetails: (article: Article) -> Unit,
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = { Text("News") },
            actions = {
                ArticleFilterActionIcon(
                    filterState = articlesFilterEditorState,
                )
            },
        )
    },
    content = { padding ->
        Column {
            ArticleList(
                modifier = Modifier.padding(padding),
                listState = articleListState,
                onNavigateToDetails = onNavigateToDetails,
            )

            ArticleFilter(filterState = articlesFilterEditorState)

            Toaster(state = toasterState)
        }
    },
    floatingActionButton = {
        AudioCommandButton(state = audioCommandButtonState)
    },
)
