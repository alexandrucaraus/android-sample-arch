package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class ObserveArticles(
    private val newsApi: NewsApi,
    private val localeStore: LocaleStore,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun stream(filter: ArticleFilter): Flow<Result<List<Article>, Error>> =
        flowOf(filter).flatMapLatest {
            if (filter == ArticleFilter()) headlines() else everything(filter)
        }

    private fun headlines(): Flow<Result<List<Article>, Error>> = flow {
        emit(
            newsApi.getHeadlines(
                language = localeStore.getLanguageCode(),
            ),
        )
    }.catch { cause: Throwable ->
        emit(Result.error(Error("networkError", cause.message ?: "Unknown error")))
    }.filterRemovedArticles()

    private fun everything(filter: ArticleFilter): Flow<Result<List<Article>, Error>> = flow {
        emit(newsApi.getEverything(filter))
    }.catch {
        emit(Result.error(Error("networkError", it.message ?: "Unknown error")))
    }.filterRemovedArticles()

    private fun Flow<Result<List<Article>, Error>>.filterRemovedArticles() = map {
        if (it.isSuccess) {
            Result.success(
                it.success?.filterNot { article ->
                    article.title.contains(
                        "removed",
                        ignoreCase = true,
                    )
                }.orEmpty(),
            )
        } else {
            it
        }
    }
}