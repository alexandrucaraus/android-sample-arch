@file:Suppress("OPT_IN_USAGE")

package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germanautolabs.acaraus.data.SpeechEvent
import com.germanautolabs.acaraus.data.SpeechRecognizer
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandState
import com.germanautolabs.acaraus.screens.components.ToasterState
import com.germanautolabs.acaraus.usecase.GetArticles
import com.germanautolabs.acaraus.usecase.GetArticlesLanguages
import com.germanautolabs.acaraus.usecase.GetArticlesSources
import com.germanautolabs.acaraus.usecase.GetLocale
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
    private val getArticles: GetArticles,
    getArticlesSources: GetArticlesSources,
    setLocale: SetLocale,
    getLocale: GetLocale,
    newsLanguage: GetArticlesLanguages,
    private val speechRecognizer: SpeechRecognizer,
) : ViewModel() {

    private val defaultArticleListState = ArticleListState(
        reload = ::reload,
    )
    val articleListState = MutableStateFlow(defaultArticleListState)

    private val filterStateHolder = ArticleFilterStateHolder(
        getArticlesSources = getArticlesSources,
        setLocale = setLocale,
        getLocale = getLocale,
        newsLanguage = newsLanguage,
        currentScope = viewModelScope,
    )

    private val currentFilter = filterStateHolder.currentFilter
    val filterEditorState = filterStateHolder.filterEditorState

    val audioCommandState = MutableStateFlow(
        AudioCommandState(
            hasSpeechRecognition = speechRecognizer.isAvailable.value,
            toggleListening = ::toggleListening,
        ),
    )

    val toasterState = MutableStateFlow(
        ToasterState(
            resetToast = ::resetToast,
        ),
    )

    private val reloadCommand = MutableSharedFlow<Unit>()

    init {
        merge(reloadCommand, filterStateHolder.currentFilter)
            .map { currentFilter.value }
            .onEach { articleListState.update { it.copy(isLoading = true) } }
            .flatMapLatest(getArticles::stream)
            .onEach(::updateArticleListState)
            .launchIn(viewModelScope)

        speechRecognizer.isListening.onEach { isListening ->
            audioCommandState.update { it.copy(isListening = isListening) }
        }.launchIn(viewModelScope)

        speechRecognizer.events().onEach { event ->
            when (event) {
                is SpeechEvent.Result -> reloadVoiceCommand(event.matches)
                is SpeechEvent.Error -> showToast(event.errorMessage)
                is SpeechEvent.RmsChanged -> audioCommandState.update { it.copy(audioInputChangesDb = event.rmsdB) }
                else -> { /* no op */ }
            }
        }.launchIn(viewModelScope)
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

    //
    private fun reloadVoiceCommand(spokenWords: List<String>) {
        spokenWords.find { it.contains("Reload", ignoreCase = true) }?.let {
            showToast("Received reloading command...")
            reload()
        } ?: {
            showToast("Command not recognized")
        }
    }

    private fun reload() {
        viewModelScope.launch { reloadCommand.emit(Unit) }
    }

    private fun toggleListening() {
        if (speechRecognizer.isListening.value) {
            speechRecognizer.stopListening()
        } else {
            speechRecognizer.startListening()
        }
    }

    private fun showToast(message: String) {
        toasterState.update { it.copy(showToast = true, message = message) }
    }

    private fun resetToast() {
        toasterState.update { it.copy(showToast = false, message = "") }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
    }
}
