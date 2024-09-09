package eu.acaraus.news.domain.usecases

import eu.acaraus.core.Either
import eu.acaraus.core.map
import eu.acaraus.core.onEachSuccess
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.toNewsError
import eu.acaraus.news.domain.repositories.LocaleRepository
import eu.acaraus.news.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import java.util.concurrent.atomic.AtomicReference

@Factory
class GetArticlesSources(
    private val newsRepository: NewsRepository,
    private val localeRepository: LocaleRepository,
) {

    // Todo remove cache from usecase
    private val atomicArticleSources = AtomicReference<List<ArticlesSources>>(emptyList())

    operator fun invoke(): Flow<Either<NewsError, List<ArticlesSources>>> = flow {
        if (atomicArticleSources.get().isEmpty()) {
            newsRepository
                .getSources()
                .onEachSuccess { atomicArticleSources.set(it) }
        }
        emit(Either.success<NewsError, List<ArticlesSources>>(atomicArticleSources.get()))
    }.map { sourcesResult ->
        sourcesResult.map { sources -> sources.filter { it.language == localeRepository.getLanguageCode() } }
    }.catch { cause ->
        emit(Either.error(cause.toNewsError()))
    }

    fun bySourceName(name: String) =
        atomicArticleSources.get()
            .firstOrNull { item -> item.name == name }
            ?.let(::listOf) ?: emptyList()

    fun bySourceLanguageCode(languageCode: String) =
        atomicArticleSources.get()
            .filter { item -> item.language == languageCode }
}
