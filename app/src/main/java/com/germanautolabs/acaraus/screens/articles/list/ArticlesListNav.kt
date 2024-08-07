package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.germanautolabs.acaraus.infra.Dispatchers
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesFilterStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesListStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.SpeechRecognizerStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Serializable
object ArticleListNode

fun NavGraphBuilder.articlesListNavNode(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (article: Article) -> Unit,
) {
    composable<ArticleListNode> {
        // todo : simplify this vm creation
        val dispatchers = koinInject<Dispatchers>()
        val scope = CoroutineScope(SupervisorJob() + dispatchers.ui)
        val articlesListHolder = koinInject<ArticlesListStateHolder> { parametersOf(scope) }
        val filterStateHolder = koinInject<ArticlesFilterStateHolder> { parametersOf(scope) }
        val speechRecognizerStateHolder =
            koinInject<SpeechRecognizerStateHolder> { parametersOf(scope) }
        val vm = koinViewModel<ArticlesListViewModel> {
            parametersOf(
                articlesListHolder,
                speechRecognizerStateHolder,
                filterStateHolder,
                scope,
            )
        }
        ArticleListScreen(
            modifier = modifier,
            articleListState = vm.articlesListState.collectAsStateWithLifecycle().value,
            articlesFilterEditorState = vm.articlesFilterUiState.collectAsStateWithLifecycle().value,
            audioCommandButtonState = vm.audioCommandButtonState.collectAsStateWithLifecycle().value,
            toasterState = vm.toasterState.collectAsStateWithLifecycle().value,
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}
