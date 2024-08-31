package eu.acaraus.news.presentation.list

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.presentation.list.holders.ArticlesListKoinScope
import eu.acaraus.shared.lib.koin.scopedKoinViewModel
import kotlinx.serialization.Serializable

@Serializable
object ArticleListNode

fun NavGraphBuilder.articlesListNavNode(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (article: Article) -> Unit,
) {
    composable<ArticleListNode> {
        val vm = scopedKoinViewModel<ArticlesListViewModel, ArticlesListKoinScope>()
        ArticleListScreen(
            modifier = modifier,
            articleListState = vm.articlesUiState.collectAsStateWithLifecycle().value,
            articlesFilterEditorState = vm.articlesFilterUiState.collectAsStateWithLifecycle().value,
            audioCommandButtonState = vm.audioCommandButtonUiState.collectAsStateWithLifecycle().value,
            toasterState = vm.toasterState.collectAsStateWithLifecycle().value,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}
