@file:Suppress("OPT_IN_USAGE")

package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.usecase.GetLocale
import com.germanautolabs.acaraus.usecase.GetNewsLanguage
import com.germanautolabs.acaraus.usecase.ObserveArticles
import com.germanautolabs.acaraus.usecase.ObserveSources
import com.germanautolabs.acaraus.usecase.SetLocale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticleListViewModel(
    private val observeArticles: ObserveArticles,
    private val observeSources: ObserveSources,
    private val setLocale: SetLocale,
    private val getLocale: GetLocale,
    private val newsLanguage: GetNewsLanguage,
) : ViewModel() {

    private val defaultArticleListState = ArticleListState(
        retry = ::retryLoading,
        toggleListening = ::toggleListening,
    )
    val articleListState = MutableStateFlow(defaultArticleListState)
    private val articleListLoadRetry = MutableSharedFlow<Unit>()

    private val filterStateHolder = ArticleFilterStateHolder(
        observeSources = observeSources,
        setLocale = setLocale,
        getLocale = getLocale,
        newsLanguage = newsLanguage,
        currentScope = viewModelScope,
    )

    val filterEditorState = filterStateHolder.filterEditorState
    val currentFilter = filterStateHolder.currentFilter

    init {
        merge(articleListLoadRetry, filterStateHolder.currentFilter)
            .map { currentFilter.value }
            .onEach { articleListState.update { it.copy(isLoading = true) } }
            .flatMapLatest(observeArticles::stream)
            .onEach(::updateArticleListState)
            .launchIn(viewModelScope)
    }

    private fun updateArticleListState(result: Result<List<Article>, Error>) {
        when {
            result.isSuccess -> articleListState.update {
                it.copy(
                    list = result.success.orEmpty(),
                    isLoading = false,
                    isError = false,
                    errorMessage = null,
                )
            }

            result.isError -> {
                articleListState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.error?.message,
                    )
                }
            }
        }
    }

    private fun retryLoading() {
        viewModelScope.launch { articleListLoadRetry.emit(Unit) }
    }

    private fun toggleListening() {}
}
