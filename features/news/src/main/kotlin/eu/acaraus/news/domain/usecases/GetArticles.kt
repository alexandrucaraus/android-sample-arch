package eu.acaraus.news.domain.usecases

import eu.acaraus.core.Either
import eu.acaraus.core.map
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.toNewsError
import eu.acaraus.news.domain.repositories.LocaleRepository
import eu.acaraus.news.domain.repositories.NewsRepository
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
    private val newsRepository: NewsRepository,
    private val localeRepository: LocaleRepository,
) {

    operator fun invoke(filter: ArticlesFilter): Flow<Either<NewsError, List<Article>>> =
        flowOf(filter)
            .chooseArticlesEndpoint()
            .filterRemovedArticles()
            .catch { cause -> emit(Either.Error(cause.toNewsError())) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<ArticlesFilter>.chooseArticlesEndpoint() = flatMapLatest { filter ->
        if (filter.isDefault()) fetchHeadlines() else fetchEverything(filter)
    }

    private fun ArticlesFilter.isDefault() = this == ArticlesFilter()

    private fun fetchHeadlines(): Flow<Either<NewsError, List<Article>>> = flow {
        emit(newsRepository.getHeadlines(language = localeRepository.getLanguageCode()))
    }

    private fun fetchEverything(filter: ArticlesFilter): Flow<Either<NewsError, List<Article>>> =
        flow {
            emit(newsRepository.getEverything(filter))
        }

    private fun Flow<Either<NewsError, List<Article>>>.filterRemovedArticles() =
        map { result ->
            result.map { articles ->
                articles.filterNot { article ->
                    article.title.lowercase(Locale.getDefault()) == "removed"
                }
            }
        }
}
