package com.germanautolabs.acaraus.screens.articles.list.holders

import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.onEachError
import com.germanautolabs.acaraus.models.onEachSuccess
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.usecase.GetArticles
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
                updateError(Error(code = "unknown", exception.message ?: "Failed to load articles"))
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

    private fun updateError(error: Error) {
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
