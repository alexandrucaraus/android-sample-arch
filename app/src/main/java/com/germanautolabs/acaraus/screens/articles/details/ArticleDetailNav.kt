package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.germanautolabs.acaraus.main.serializableType
import com.germanautolabs.acaraus.models.Article
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Serializable
data class ArticleDetailNode(val article: Article)

fun NavController.onNavigateToArticleDetail(article: Article) = navigate(ArticleDetailNode(article))

fun NavGraphBuilder.articleDetailNavNode(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    composable<ArticleDetailNode>(
        typeMap = mapOf(typeOf<Article>() to serializableType<Article>()),
    ) { backStack ->
        val article = backStack.toRoute<ArticleDetailNode>().article
        val vm = koinViewModel<ArticleDetailViewModel> { parametersOf(article) }
        ArticleDetailScreen(
            modifier = modifier,
            state = vm.state.collectAsStateWithLifecycle().value,
            onNavigateBack = onNavigateBack,
        )
    }
}
