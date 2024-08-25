package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.map
import com.germanautolabs.acaraus.models.onEachSuccess
import com.germanautolabs.acaraus.models.toError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import java.util.concurrent.atomic.AtomicReference

@Factory
class GetArticlesSources(
    private val newsApi: NewsApi,
    private val localeStore: LocaleStore,
) {

    private val atomicArticleSources = AtomicReference<List<ArticlesSources>>(emptyList())

    operator fun invoke(): Flow<Result<List<ArticlesSources>, Error>> = flow {
        if (atomicArticleSources.get().isEmpty()) {
            newsApi
                .getSources()
                .onEachSuccess { atomicArticleSources.set(it) }
        }
        emit(Result.success<List<ArticlesSources>, Error>(atomicArticleSources.get()))
    }.map { sourcesResult ->
        sourcesResult.map { sources -> sources.filter { it.language == localeStore.getLanguageCode() } }
    }.catch { cause ->
        emit(Result.error(cause.toError()))
    }

    fun bySourceName(name: String) =
        atomicArticleSources.get()
            .firstOrNull { item -> item.name == name }
            ?.let(::listOf) ?: emptyList()

    fun bySourceLanguageCode(languageCode: String) =
        atomicArticleSources.get()
            .filter { item -> item.language == languageCode }
}
