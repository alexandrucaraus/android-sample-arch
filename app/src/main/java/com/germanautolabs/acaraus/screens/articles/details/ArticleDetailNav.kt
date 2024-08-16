package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.germanautolabs.acaraus.lib.scopedKoinViewModel
import com.germanautolabs.acaraus.lib.serializableNavType
import com.germanautolabs.acaraus.models.Article
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ArticleDetailNode(
    @SerialName(ArticleDetailViewModel.ARTICLE_KEY) val article: Article,
)

fun NavController.onNavigateToArticleDetail(article: Article) =
    navigate(ArticleDetailNode(article))

fun NavGraphBuilder.articleDetailNavNode(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    composable<ArticleDetailNode>(
        typeMap = mapOf(typeOf<Article>() to serializableNavType<Article>()),
    ) { backStack: NavBackStackEntry ->
        val article = backStack.toRoute<ArticleDetailNode>().article
        val vm = scopedKoinViewModel<ArticleDetailViewModel>(article)
        ArticleDetailScreen(
            modifier = modifier,
            state = vm.state.collectAsStateWithLifecycle().value,
            onNavigateBack = onNavigateBack,
        )
    }
}
