package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.models.Article
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

const val ARTICLE_DETAIL_ROUTE = "article-detail"
private const val ARTICLE_ID_PARAM = "article-id"

fun NavController.onNavigateToDetails(article: Article) =
    navigate("$ARTICLE_DETAIL_ROUTE/?$ARTICLE_ID_PARAM=${article.id}")

fun NavGraphBuilder.articleDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    composable(route = "$ARTICLE_DETAIL_ROUTE/?$ARTICLE_ID_PARAM={$ARTICLE_ID_PARAM}") { backStack ->
        val articleId = backStack.arguments?.getString(ARTICLE_ID_PARAM)
        val vm = koinViewModel<ArticleDetailViewModel> { parametersOf(articleId) }
        ArticleDetailScreen(
            modifier = modifier,
            vm = vm,
            onNavigateBack = onNavigateBack,
        )
    }
}
