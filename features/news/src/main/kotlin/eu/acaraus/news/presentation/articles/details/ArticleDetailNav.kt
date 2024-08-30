package eu.acaraus.news.presentation.articles.details

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.lib.scopedKoinViewModel
import eu.acaraus.news.lib.serializableNavType
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
