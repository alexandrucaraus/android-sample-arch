package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.models.Article
import org.koin.androidx.compose.koinViewModel

const val ARTICLE_LIST_ROUTE = "article-list"

fun NavGraphBuilder.articleListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (article: Article) -> Unit,
) {
    composable(route = ARTICLE_LIST_ROUTE) {
        val vm = koinViewModel<ArticleListViewModel>()
        ArticleListScreen(
            modifier = modifier,
            articleListState = vm.listState.collectAsStateWithLifecycle().value,
            articleFilterState = vm.filterState.collectAsStateWithLifecycle().value,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}
