package eu.acaraus.news.presentation.details

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.shared.lib.android.serializableNavType
import eu.acaraus.shared.lib.koin.scopedKoinViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ArticleDetailNode(
    val article: Article,
)

fun NavController.onNavigateToArticleDetail(article: Article) {
    kotlin.runCatching {
        navigate(ArticleDetailNode(article))
    }.getOrElse { println(it) }
}

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
