package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.lib.scopedKoinViewModel
import com.germanautolabs.acaraus.lib.serializableNavType
import com.germanautolabs.acaraus.models.Article
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ArticleDetailNode(
    val article: Article,
)

fun NavController.onNavigateToArticleDetail(article: Article) =
    navigate(ArticleDetailNode(article))

fun NavGraphBuilder.articleDetailNavNode(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    composable<ArticleDetailNode>(
        typeMap = mapOf(typeOf<Article>() to serializableNavType<Article>()),
    ) {
        val vm = scopedKoinViewModel<ArticleDetailViewModel, ArticleDetailKoinScope>()
        ArticleDetailScreen(
            modifier = modifier,
            state = vm.state.collectAsStateWithLifecycle().value,
            onNavigateBack = onNavigateBack,
        )
    }
}
