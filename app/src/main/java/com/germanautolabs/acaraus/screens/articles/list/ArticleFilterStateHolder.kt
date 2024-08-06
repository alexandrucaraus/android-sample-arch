package com.germanautolabs.acaraus.screens.articles.list

import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.usecase.GetArticlesLanguages
import com.germanautolabs.acaraus.usecase.GetArticlesSources
import com.germanautolabs.acaraus.usecase.GetLocale
import com.germanautolabs.acaraus.usecase.SetLocale
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
import java.time.LocalDate

@Suppress("OPT_IN_USAGE")
class ArticleFilterStateHolder(
    getArticlesSources: GetArticlesSources,
    private val setLocale: SetLocale,
    private val getLocale: GetLocale,
    private val newsLanguage: GetArticlesLanguages,
    currentScope: CoroutineScope,
) {

    private val defaultArticlesFilterUiState = ArticleFilterState(
        show = ::show,
        hide = ::hide,
        setQuery = ::setQuery,
        setSortBy = ::setSortBy,
        sortByOptions = SortBy.entries.map { it.name }.toSet(),
        setSource = ::setArticleSource,
        setLanguage = ::setLanguage,
        languageOptions = newsLanguage.options(),
        setFromDate = ::setFromOldestDate,
        setToDate = ::setToNewestDate,
        reset = ::resetFilter,
        apply = ::applyFilter,
    )


    private val filterUiState = MutableStateFlow(defaultArticlesFilterUiState)
    private val filterState = MutableStateFlow(ArticlesFilter())

    val articlesFilterUiState = filterUiState.asStateFlow()
    val articlesFilterState = filterState.asStateFlow()

    private val sources = MutableStateFlow(emptyList<ArticlesSources>())
    private val reloadSourcesCommand = MutableSharedFlow<Unit>()

    init {
        merge(flowOf(Unit), reloadSourcesCommand)
            .flatMapLatest { getArticlesSources.stream() }
            .onEach { sourcesResult -> updateArticleSources(sourcesResult) }
            .launchIn(scope = currentScope)

        sources.onEach {
            filterUiState.update { it.copy(sourceOptions = buildSourceOptions()) }
        }.launchIn(scope = currentScope)
    }

    private fun updateArticleSources(result: Result<List<ArticlesSources>, Error>) {
        when {
            result.isSuccess -> sources.update { result.success.orEmpty() }
            result.isError -> { /* todo handle error */ }
        }
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
        val sortOrder = SortBy.entries.find { it.name == sortBy } ?: SortBy.MostRecent
        filterUiState.update { it.copy(sortBy = sortOrder.name) }
    }

    private fun setArticleSource(source: String) {
        val articleSource = sources.value.find { it.name == source } ?: return
        filterUiState.update {
            it.copy(source = articleSource.name)
        }
    }

    private fun setLanguage(language: String) {
        val languageCode = newsLanguage.getLanguageCodeByName(language)
        val source =
            if (getLocale.languageCode() !== languageCode) "All" else filterUiState.value.source
        filterUiState.update {
            it.copy(
                language = language,
                source = source,
                sourceOptions = buildSourceOptions(languageCode),
            )
        }
        setLocale.languageCode(languageCode)
    }

    private fun setFromOldestDate(date: LocalDate) {
        filterUiState.update { it.copy(fromOldestDate = date) }
    }

    private fun setToNewestDate(date: LocalDate) {
        filterUiState.update { it.copy(toNewestDate = date) }
    }

    private fun resetFilter() {
        filterState.update { ArticlesFilter() }
        filterUiState.update {
            defaultArticlesFilterUiState.copy(
                language = newsLanguage.getLanguageCodeByName(getLocale.languageCode()),
                sourceOptions = buildSourceOptions(),
            )
        }
    }

    private fun applyFilter() {
        filterState.update { filterUiState.value.toArticleFilter() }
        filterUiState.update { it.copy(isVisible = false) }
    }

    private fun buildSourceOptions(
        languageCode: String = getLocale.languageCode(),
    ): Set<String> =
        setOf("All") + sources.value.filter { it.language == languageCode }.map { it.name }

    private fun ArticleFilterState.toArticleFilter(): ArticlesFilter = ArticlesFilter(
        query = query,
        sortedBy = sortBy.let { SortBy.valueOf(it) },
        language = getLocale.languageCode(),
        sources = sources.value.filter { it.name == source },
        fromDate = fromOldestDate,
        toDate = toNewestDate,
    )
}
