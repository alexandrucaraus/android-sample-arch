package com.germanautolabs.acaraus.screens.articles.list.holders

import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.models.fold
import com.germanautolabs.acaraus.screens.articles.list.components.ArticlesFilterEditorState
import com.germanautolabs.acaraus.usecase.GetArticlesLanguages
import com.germanautolabs.acaraus.usecase.GetArticlesSources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import java.time.LocalDate

@Factory
class ArticlesFilterStateHolder(
    private val getArticlesSources: GetArticlesSources,
    private val getArticleLanguages: GetArticlesLanguages,
    @InjectedParam scope: CoroutineScope,
) : CoroutineScope by scope {

    private val defaultArticlesFilterUiState = ArticlesFilterEditorState(
        show = ::show,
        hide = ::hide,
        setQuery = ::setQuery,
        setSortBy = ::setSortBy,
        sortByOptions = SortBy.toSet(),
        setSource = ::setArticleSource,
        setLanguage = ::setLanguage,
        languageOptions = getArticleLanguages.options(),
        setFromDate = ::setFromOldestDate,
        setToDate = ::setToNewestDate,
        reset = ::resetFilter,
        apply = ::applyFilter,
    )

    private val filterUiState = MutableStateFlow(defaultArticlesFilterUiState)
    private val filterState = MutableStateFlow(ArticlesFilter())

    private val reloadSourcesCommand = MutableSharedFlow<Unit>()

    val articlesFilterUiState = filterUiState.asStateFlow()
    val articlesFilterState = filterState.asStateFlow()

    // Todo handle get article sources failure
    init {
        @Suppress("OPT_IN_USAGE")
        merge(flowOf(Unit), reloadSourcesCommand)
            .flatMapLatest { getArticlesSources() }
            .map { result ->
                result.fold(
                    onSuccess = { sources ->
                        filterUiState.update { it.copy(sourceOptions = sources.asSourceOptions()) }
                    },
                )
            }.launchIn(this)

        filterUiState
            .map { it.language }
            .map { getArticleLanguages.getLanguageCodeByName(it) }
            .map { getArticlesSources.bySourceLanguageCode(it) }
            .onEach { sources ->
                filterUiState.update { it.copy(sourceOptions = sources.asSourceOptions()) }
            }
            .launchIn(this)
    }

    private fun show() {
        filterUiState.update { it.copy(isVisible = true) }
    }

    private fun hide() {
        filterUiState.update { it.copy(isVisible = false) }
    }

    private fun setQuery(query: String) {
        filterUiState.update { it.copy(query = query) }
    }

    private fun setSortBy(sortBy: String) {
        filterUiState.update { it.copy(sortBy = sortBy) }
    }

    private fun setArticleSource(source: String) {
        filterUiState.update { it.copy(source = source) }
    }

    private fun setLanguage(language: String) {
        filterUiState.update { it.copy(language = language) }
    }

    private fun setFromOldestDate(date: LocalDate) {
        filterUiState.update { it.copy(fromOldestDate = date) }
    }

    private fun setToNewestDate(date: LocalDate) {
        filterUiState.update { it.copy(toNewestDate = date) }
    }

    private fun resetFilter() {
        filterUiState.update {
            defaultArticlesFilterUiState.copy(
                sourceOptions = it.sourceOptions,
                language = it.language,
            )
        }
        filterState.update { filterUiState.value.toArticleFilter() }
    }

    private fun applyFilter() {
        filterState.update { filterUiState.value.toArticleFilter() }
        filterUiState.update { it.copy(isVisible = false) }
    }

    private fun List<ArticlesSources>.asSourceOptions(): Set<String> =
        setOf("All") + map { it.name }

    private fun ArticlesFilterEditorState.toArticleFilter(): ArticlesFilter = ArticlesFilter(
        query = query,
        sortedBy = SortBy.valueOf(sortBy),
        language = getArticleLanguages.getLanguageCodeByName(language),
        sources = getArticlesSources.bySourceName(source),
        fromDate = fromOldestDate,
        toDate = toNewestDate,
    )
}
