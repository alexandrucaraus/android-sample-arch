package eu.acaraus.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.acaraus.news.presentation.articles.details.articleDetailNavNode
import eu.acaraus.news.presentation.articles.details.onNavigateToArticleDetail
import eu.acaraus.news.presentation.articles.list.ArticleListNode
import eu.acaraus.news.presentation.articles.list.articlesListNavNode

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
