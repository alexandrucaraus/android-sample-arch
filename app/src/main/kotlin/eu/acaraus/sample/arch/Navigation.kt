package eu.acaraus.sample.arch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.acaraus.news.presentation.details.articleDetailNavNode
import eu.acaraus.news.presentation.details.onNavigateToArticleDetail
import eu.acaraus.news.presentation.list.ArticleListNode
import eu.acaraus.news.presentation.list.articlesListNavNode

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = ArticleListNode,
) = NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
) {
    articlesListNavNode(
        modifier = modifier,
        onNavigateToDetails = navController::onNavigateToArticleDetail,
    )

    articleDetailNavNode(
        modifier = modifier,
        onNavigateBack = navController::popBackStack,
    )
}
