package eu.acaraus.news.domain.usecases

import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.toNewsError
import eu.acaraus.news.domain.repositories.LocaleStore
import eu.acaraus.news.domain.repositories.NewsApi
import eu.acaraus.shared.lib.Either
import eu.acaraus.shared.lib.map
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

    operator fun invoke(filter: ArticlesFilter): Flow<Either<List<Article>, NewsError>> =
        flowOf(filter)
            .chooseArticlesEndpoint()
            .filterRemovedArticles()
            .catch { cause -> emit(Either.Error(cause.toNewsError())) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<ArticlesFilter>.chooseArticlesEndpoint() = flatMapLatest { filter ->
        if (filter.isDefault()) fetchHeadlines() else fetchEverything(filter)
    }

    private fun ArticlesFilter.isDefault() = this == ArticlesFilter()

    private fun fetchHeadlines(): Flow<Either<List<Article>, NewsError>> = flow {
        emit(newsApi.getHeadlines(language = localeStore.getLanguageCode()))
    }

    private fun fetchEverything(filter: ArticlesFilter): Flow<Either<List<Article>, NewsError>> = flow {
        emit(newsApi.getEverything(filter))
    }

    private fun Flow<Either<List<Article>, NewsError>>.filterRemovedArticles() = map { result ->
        result.map { articles ->
            articles.filterNot { article ->
                article.title.lowercase(Locale.getDefault()) == "removed"
            }
        }
    }
}
