package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.models.Article
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object ArticleListNode

fun NavGraphBuilder.articleListNavNode(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (article: Article) -> Unit,
) {
    composable<ArticleListNode> {
        val vm = koinViewModel<ArticleListViewModel>()
        ArticleListScreen(
            modifier = modifier,
            articleListState = vm.articleListState.collectAsStateWithLifecycle().value,
            articleFilterState = vm.articlesFilterUiState.collectAsStateWithLifecycle().value,
            audioCommandState = vm.audioCommandState.collectAsStateWithLifecycle().value,
            toasterState = vm.toasterState.collectAsStateWithLifecycle().value,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}
