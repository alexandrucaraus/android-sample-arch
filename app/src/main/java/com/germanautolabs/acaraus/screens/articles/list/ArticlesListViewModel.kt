package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesFilterStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesListStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.SpeechRecognizerStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class ArticlesListViewModel(
    @InjectedParam private val articlesListStateHolder: ArticlesListStateHolder,
    @InjectedParam private val speechRecognizerStateHolder: SpeechRecognizerStateHolder,
    @InjectedParam private val filterStateHolder: ArticlesFilterStateHolder,
    @InjectedParam coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    val articlesUiState = articlesListStateHolder.listUi

    val articlesFilterUiState = filterStateHolder.filtersUi

    val audioCommandButtonUiState = speechRecognizerStateHolder.audioCommandButtonUi

    val toasterState = speechRecognizerStateHolder.toasterUi

    init {
        filterStateHolder
            .activeFilter
            .onEach(articlesListStateHolder::reloadArticles)
            .launchIn(viewModelScope)

        @Suppress("OPT_IN_USAGE")
        speechRecognizerStateHolder
            .reloadCommand
            .flatMapLatest {
                filterStateHolder.activeFilter
            }
            .onEach(articlesListStateHolder::reloadArticles)
            .launchIn(viewModelScope)
    }
}
