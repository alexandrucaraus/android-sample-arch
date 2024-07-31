package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.dummyArticleList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory

@KoinViewModel
class ArticleListViewModel(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val defaultArticleListState = ArticleListState(
        load = ::loadArticles,
        toggleListening = ::toggleListening,
    )

    private val defaultFilterState = ArticleFilterState(
        setQuery = ::setFilterQuery,
    )

    val listState = MutableStateFlow(defaultArticleListState)
    val filterState = MutableStateFlow(defaultFilterState)

    init {
        loadArticles()
    }

    private var job: Job? = null
    private fun loadArticles() {
        job?.cancel()
        listState.update { it.copy(isLoading = true) }
        job = viewModelScope.launch {
            val result = articleRepository.get(ArticleRepository.Params(filterState.value.filter))
            when {
                result.isSuccess ->
                    listState.update {
                        it.copy(
                            list = result.success!!,
                            isLoading = false,
                            isError = false,
                            errorMessage = null,
                        )
                    }
                result.isError -> {
                    listState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.error!!,
                        )
                    }
                }
            }
        }
    }

    private fun toggleListening() {
    }

    private fun setFilterQuery(query: String) {
    }
}

data class ArticleListState(
    val list: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isListening: Boolean = false,
    val load: () -> Unit,
    val toggleListening: () -> Unit,
)

data class ArticleFilterState(
    val filter: ArticleFilter = ArticleFilter(),
    val setQuery: (String) -> Unit,
)

@Factory
class ArticleRepository {

    suspend fun get(param: Params): Result<List<Article>, String> {
        return Result.success(dummyArticleList)
    }

    data class Params(
        val filter: ArticleFilter,
    )
}
