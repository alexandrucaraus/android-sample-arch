package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesFilterStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesListStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.SpeechRecognizerStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class ArticlesListViewModel(
    @InjectedParam private val articlesListStateStateHolder: ArticlesListStateHolder,
    @InjectedParam private val speechRecognizerStateHolder: SpeechRecognizerStateHolder,
    @InjectedParam private val filterStateHolder: ArticlesFilterStateHolder,
    @InjectedParam coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    val articlesFilterUiState = filterStateHolder.articlesFilterUiState

    val articlesListState = articlesListStateStateHolder.articleListState

    val audioCommandButtonState = speechRecognizerStateHolder.audioCommandButton

    val toasterState = speechRecognizerStateHolder.toaster

    init {
        filterStateHolder
            .articlesFilterState
            .onEach(articlesListStateStateHolder::reloadArticles)
            .launchIn(viewModelScope)

        speechRecognizerStateHolder
            .reloadCommand
            .map { filterStateHolder.articlesFilterState.value }
            .onEach(articlesListStateStateHolder::reloadArticles)
            .launchIn(viewModelScope)
    }
}
