package com.germanautolabs.acaraus.screens.articles.list

import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.usecase.GetArticlesLanguages
import com.germanautolabs.acaraus.usecase.GetArticlesSources
import com.germanautolabs.acaraus.usecase.GetLocale
import com.germanautolabs.acaraus.usecase.SetLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class ArticleFilterStateHolder(
    getArticlesSources: GetArticlesSources,
    private val setLocale: SetLocale,
    private val getLocale: GetLocale,
    private val newsLanguage: GetArticlesLanguages,
    currentScope: CoroutineScope,
) {

    private val defaultFilterState = ArticleFilterState(
        show = ::showFilter,
        hide = ::hideFilter,
        setQuery = ::setFilterQuery,
        setSortBy = ::setFilterSortBy,
        sortByOptions = SortBy.entries.map { it.name }.toSet(),
        setSource = ::setFilterSource,
        setLanguage = ::setFilterLanguage,
        languageOptions = newsLanguage.options(),
        setFromDate = ::setFilterFromDate,
        setToDate = ::setFilterToDate,
        reset = ::resetFilter,
        apply = ::applyFilter,
    )

    val filterEditorState = MutableStateFlow(defaultFilterState)
    val currentFilter = MutableStateFlow(ArticleFilter())

    private val articleSources = MutableStateFlow(emptyList<ArticleSource>())

    init {
        // TODO relaunch on error
        getArticlesSources.stream().onEach { updateArticleSources(it) }.launchIn(currentScope)
    }

    private fun updateArticleSources(result: Result<List<ArticleSource>, Error>) {
        println("Update article sources")
        when {
            result.isSuccess -> {
                articleSources.update { result.success.orEmpty() }
                filterEditorState.update { it.copy(sourceOptions = buildSourceOptions()) }
            }
            result.isError -> { /* todo handle error */ }
        }
    }

    private fun showFilter() {
        filterEditorState.update { it.copy(isVisible = true) }
    }

    private fun hideFilter() {
        filterEditorState.update { it.copy(isVisible = false) }
    }

    private fun setFilterQuery(query: String) {
        filterEditorState.update { it.copy(query = query) }
    }

    private fun setFilterSortBy(sortBy: String) {
        val sortOrder = SortBy.entries.find { it.name == sortBy } ?: SortBy.MostRecent
        filterEditorState.update { it.copy(sortBy = sortOrder.name) }
    }

    private fun setFilterSource(source: String) {
        val articleSource = articleSources.value.find { it.name == source } ?: return
        filterEditorState.update {
            it.copy(source = articleSource.name)
        }
    }

    private fun setFilterLanguage(language: String) {
        val languageCode = newsLanguage.getLanguageCodeByName(language)
        val source =
            if (getLocale.languageCode() !== languageCode) "All" else filterEditorState.value.source
        filterEditorState.update {
            it.copy(
                language = language,
                source = source,
                sourceOptions = buildSourceOptions(languageCode),
            )
        }
        setLocale.languageCode(languageCode)
    }

    private fun setFilterFromDate(date: LocalDate) {
        filterEditorState.update { it.copy(fromOldestDate = date) }
    }

    private fun setFilterToDate(date: LocalDate) {
        filterEditorState.update { it.copy(toNewestDate = date) }
    }

    private fun resetFilter() {
        currentFilter.update { ArticleFilter() }
        filterEditorState.update {
            defaultFilterState.copy(
                language = newsLanguage.getLanguageCodeByName(getLocale.languageCode()),
                sourceOptions = buildSourceOptions(),
            )
        }
    }

    private fun applyFilter() {
        currentFilter.update { filterEditorState.value.toArticleFilter() }
        filterEditorState.update { it.copy(isVisible = false) }
    }

    private fun buildSourceOptions(
        languageCode: String = getLocale.languageCode(),
    ): Set<String> =
        setOf("All") + articleSources.value.filter { it.language == languageCode }.map { it.name }

    private fun ArticleFilterState.toArticleFilter(): ArticleFilter = ArticleFilter(
        query = query,
        sortedBy = sortBy.let { SortBy.valueOf(it) },
        language = getLocale.languageCode(),
        sources = articleSources.value.filter { it.name == source },
        fromDate = fromOldestDate,
        toDate = toNewestDate,
    )
}
