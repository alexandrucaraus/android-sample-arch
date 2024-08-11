package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import java.util.Locale

@Factory
class GetArticles(
    private val newsApi: NewsApi,
    private val localeStore: LocaleStore,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(filter: ArticlesFilter): Flow<Result<List<Article>, Error>> =
        flowOf(filter)
            .flatMapLatest {
                if (filter == ArticlesFilter()) headlines() else everything(filter)
            }

    private fun headlines(): Flow<Result<List<Article>, Error>> = flow {
        emit(
            newsApi.getHeadlines(
                language = localeStore.getLanguageCode(),
            ),
        )
    }.catch { cause: Throwable ->
        emit(Result.Error(Error("networkError", cause.message ?: "Unknown error")))
    }.filterRemovedArticles()

    private fun everything(filter: ArticlesFilter): Flow<Result<List<Article>, Error>> = flow {
        emit(newsApi.getEverything(filter))
    }.catch {
        emit(Result.Error(Error("networkError", it.message ?: "Unknown error")))
    }.filterRemovedArticles()

    private fun Flow<Result<List<Article>, Error>>.filterRemovedArticles() = map { result ->
        result.map { articles -> articles.filterNot { it.title.lowercase(Locale.getDefault()) == "removed" } }
    }
}
