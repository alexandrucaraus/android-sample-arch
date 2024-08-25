package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesFilterStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesListKoinScope
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesListStateHolder
import com.germanautolabs.acaraus.screens.articles.list.holders.SpeechRecognizerStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Scope
import java.util.UUID

@KoinViewModel
@Scope(ArticlesListKoinScope::class)
class ArticlesListViewModel(
    private val articlesListStateHolder: ArticlesListStateHolder,
    private val filterStateHolder: ArticlesFilterStateHolder,
    speechRecognizerStateHolder: SpeechRecognizerStateHolder,
    val coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    val id = UUID.randomUUID()

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
            .flatMapLatest { filterStateHolder.activeFilter }
            .onEach(articlesListStateHolder::reloadArticles)
            .launchIn(viewModelScope)
    }
}
