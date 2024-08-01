package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.data.ArticleRepository
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

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
        show = ::toggleFilter,
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

    private fun toggleFilter() {
        filterState.update { it.copy(isVisible = !it.isVisible) }
    }
}
