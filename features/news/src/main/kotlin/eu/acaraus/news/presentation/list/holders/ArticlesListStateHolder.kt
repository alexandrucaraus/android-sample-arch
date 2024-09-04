package eu.acaraus.news.presentation.list.holders

import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.usecases.GetArticles
import eu.acaraus.news.presentation.list.components.ArticleListState
import eu.acaraus.shared.lib.onEachError
import eu.acaraus.shared.lib.onEachSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Scope

@Factory
@Scope(ArticlesListKoinScope::class)
class ArticlesListStateHolder(
    private val getArticles: GetArticles,
    scope: CoroutineScope,
) : CoroutineScope by scope {

    private val listUiState = MutableStateFlow(
        ArticleListState(
            reload = ::reloadArticles,
        ),
    )

    val listUi = listUiState.asStateFlow()

    private val reloadArticlesCommand = MutableSharedFlow<ArticlesFilter>()

    init {
        @Suppress("OPT_IN_USAGE")
        reloadArticlesCommand
            .onEach {
                updateLoading()
            }
            .flatMapLatest(getArticles::invoke)
            .mapLatest { result ->
                result.onEachSuccess(::updateSuccess).onEachError(::updateError)
            }
            .catch { exception ->
                updateError(
                    NewsError(
                        code = "unknown",
                        exception.message ?: "Failed to load articles",
                    ),
                )
            }
            .launchIn(this)
    }

    fun reloadArticles(articlesFilter: ArticlesFilter = ArticlesFilter()) {
        launch(coroutineContext) {
            reloadArticlesCommand.emit(articlesFilter)
        }
    }

    private fun updateLoading() {
        listUiState.update {
            it.copy(
                isLoading = true,
            )
        }
    }

    private fun updateSuccess(articles: List<Article>) {
        listUiState.update {
            it.copy(
                list = articles,
                isLoading = false,
                isError = false,
                errorMessage = null,
            )
        }
    }

    private fun updateError(error: NewsError) {
        listUiState.update {
            it.copy(
                list = emptyList(),
                isLoading = false,
                isError = true,
                errorMessage = error.message,
            )
        }
    }
}
