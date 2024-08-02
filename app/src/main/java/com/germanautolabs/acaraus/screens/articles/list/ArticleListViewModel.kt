@file:Suppress("OPT_IN_USAGE")

package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.infra.Dispatchers
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.usecase.ObserveArticles
import com.germanautolabs.acaraus.usecase.ObserveSources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate

@KoinViewModel
class ArticleListViewModel(
    private val observeArticles: ObserveArticles,
    private val observeSources: ObserveSources,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val defaultArticleListState = ArticleListState(
        load = ::loadArticles,
        toggleListening = ::toggleListening,
    )

    private val defaultFilterState = ArticleFilterState(
        show = ::showFilter,
        hide = ::hideFilter,
        setQuery = ::setFilterQuery,
        setSortBy = ::setFilterSortBy,
        setSource = ::setFilterSource,
        setLanguage = ::setFilterLanguage,
        setFromDate = ::setFilterFromDate,
        setToDate = ::setFilterToDate,
        reset = ::resetFilter,
        apply = ::applyFilter,
    )

    val articleListState = MutableStateFlow(defaultArticleListState)
    val filterEditorState = MutableStateFlow(defaultFilterState)

    private val filterState = MutableStateFlow(ArticleFilter())

    init {
        merge(flowOf(Unit), filterState)
            .onEach { articleListState.update { it.copy(isLoading = true) } }
            .flatMapLatest { observeArticles.stream(filterState.value) }
            .onEach(::updateArticleListState)
            .launchIn(viewModelScope)
    }

    private fun updateArticleListState(result: Result<List<Article>, Error>) {
        when {
            result.isSuccess ->
                articleListState.update {
                    it.copy(
                        list = result.success?.filterNot { article -> article.title.contains("removed", ignoreCase = true) }.orEmpty(),
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

    private fun loadArticles() {
    }

    private fun toggleListening() {
    }

    private fun showFilter() {
        filterEditorState.update { it.copy(isVisible = true) }
    }

    private fun hideFilter() {
        filterEditorState.update { it.copy(isVisible = false) }
    }

    private fun setFilterQuery(query: String) {
        filterEditorState.update { it.copy(filter = it.filter.copy(query = query)) }
    }

    private fun setFilterSortBy(sortBy: SortBy) {
        filterEditorState.update { it.copy(filter = it.filter.copy(sortedBy = sortBy)) }
    }

    private fun setFilterSource(source: ArticleSource) {
        filterEditorState.update {
            it.copy(filter = it.filter.copy(sources = listOf(source)))
        }
    }

    private fun setFilterLanguage(language: String) {
        filterEditorState.update { it.copy(filter = it.filter.copy(language = language)) }
    }

    private fun setFilterFromDate(date: LocalDate) {
        filterEditorState.update { it.copy(filter = it.filter.copy(fromDate = date)) }
    }

    private fun setFilterToDate(date: LocalDate) {
        filterEditorState.update { it.copy(filter = it.filter.copy(toDate = date)) }
    }

    private fun resetFilter() {
        filterEditorState.update { defaultFilterState }
        filterEditorState.update { it.copy(isVisible = false) }
    }

    private fun applyFilter() {
        filterState.update { filterEditorState.value.filter }
        filterEditorState.update { it.copy(isVisible = false) }
    }
}
