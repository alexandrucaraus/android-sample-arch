package eu.acaraus.news.domain.usecases

import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.Either
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.map
import eu.acaraus.news.domain.entities.onEachSuccess
import eu.acaraus.news.domain.entities.toNewsError
import eu.acaraus.news.domain.repositories.LocaleStore
import eu.acaraus.news.domain.repositories.NewsApi
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

    operator fun invoke(): Flow<Either<List<ArticlesSources>, NewsError>> = flow {
        if (atomicArticleSources.get().isEmpty()) {
            newsApi
                .getSources()
                .onEachSuccess { atomicArticleSources.set(it) }
        }
        emit(Either.success<List<ArticlesSources>, NewsError>(atomicArticleSources.get()))
    }.map { sourcesResult ->
        sourcesResult.map { sources -> sources.filter { it.language == localeStore.getLanguageCode() } }
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
