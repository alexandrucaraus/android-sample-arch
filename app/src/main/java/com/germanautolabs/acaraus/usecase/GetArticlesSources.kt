package com.germanautolabs.acaraus.usecase

import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
class GetArticlesSources(
    private val newsApi: NewsApi,
    private val localeStore: LocaleStore,
) {
    fun stream(): Flow<Result<List<ArticlesSources>, Error>> = flow {
        emit(
            newsApi.getSources(
                language = localeStore.getLanguageCode(),
            ),
        )
    }.catch {
        emit(Result.error(Error(code = "observeSourcesError", it.message ?: "Unknown error")))
    }
}
