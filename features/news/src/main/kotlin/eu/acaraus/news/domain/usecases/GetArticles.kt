package eu.acaraus.news.domain.usecases

import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.Either
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.map
import eu.acaraus.news.domain.entities.toNewsError
import eu.acaraus.news.domain.repositories.LocaleStore
import eu.acaraus.news.domain.repositories.NewsApi
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
    operator fun invoke(filter: ArticlesFilter): Flow<Either<List<Article>, NewsError>> =
        flowOf(filter)
            .flatMapLatest {
                if (filter == ArticlesFilter()) headlines() else everything(filter)
            }

    private fun headlines(): Flow<Either<List<Article>, NewsError>> = flow {
        emit(newsApi.getHeadlines(language = localeStore.getLanguageCode()))
    }.catch { cause ->
        emit(Either.Error(cause.toNewsError()))
    }.filterRemovedArticles()

    private fun everything(filter: ArticlesFilter): Flow<Either<List<Article>, NewsError>> = flow {
        emit(newsApi.getEverything(filter))
    }.catch { cause ->
        emit(Either.Error(cause.toNewsError()))
    }.filterRemovedArticles()

    private fun Flow<Either<List<Article>, NewsError>>.filterRemovedArticles() = map { result ->
        result.map { articles ->
            articles.filterNot { article ->
                article.title.lowercase(Locale.getDefault()) == "removed"
            }
        }
    }
}
