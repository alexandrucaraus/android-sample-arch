package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.lib.scopedKoinViewModel
import com.germanautolabs.acaraus.models.Article
import kotlinx.serialization.Serializable

@Serializable
object ArticleListNode

fun NavGraphBuilder.articlesListNavNode(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (article: Article) -> Unit,
) {
    composable<ArticleListNode> {
        val vm = scopedKoinViewModel<ArticlesListViewModel>()
        ArticleListScreen(
            modifier = modifier,
            articleListState = vm.articlesUiState.collectAsStateWithLifecycle().value,
            articlesFilterEditorState = vm.articlesFilterUiState.collectAsStateWithLifecycle().value,
            audioCommandButtonState = vm.audioCommandButtonUiState.collectAsStateWithLifecycle().value,
            toasterState = vm.toasterState.collectAsStateWithLifecycle().value,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}
