package eu.acaraus.news.data.remote

import eu.acaraus.news.data.remote.client.HttpClientConfig
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.SortBy
import eu.acaraus.news.domain.repositories.NewsApi
import eu.acaraus.shared.lib.Either
import eu.acaraus.shared.lib.Either.Success
import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import java.time.format.DateTimeFormatter

@Factory(binds = [NewsApi::class])
class NewsApiImpl(
    private val httpClient: HttpClient,
    private val httpClientConfig: HttpClientConfig,
    private val dispatchers: DispatcherProvider,
) : NewsApi {

    override suspend fun getHeadlines(
        language: String,
        category: String,
    ): Either<List<Article>, NewsError> = withContext(dispatchers.io) {
        kotlin.runCatching {

            val response = httpClient.get(path("/v2/top-headlines")) {
                parameter(LANGUAGE, language)
                parameter(CATEGORY, category)
            }.body<NewsApiResponse>()

            when (response) {
                is NewsApiArticles ->
                    response.articles.map(NewsApiArticle::toArticle).let(::Success)

                else -> Either.Error(response.toError())
            }

        }.getOrElse { failure -> Either.error(failure.toError()) }
    }

    override suspend fun getSources(): Either<List<ArticlesSources>, NewsError> =
        withContext(dispatchers.io) {
            kotlin.runCatching {
                val response: NewsApiResponse =
                    httpClient.get(path("/v2/top-headlines/sources")).body()

                when (response) {
                    is NewsApiSources ->
                        response
                            .sources
                            .map(NewsApiSource::toArticleSource)
                            .let(::Success)

                    else -> Either.Error(response.toError())
                }

            }.getOrElse { failure -> Either.error(failure.toError()) }
        }

    override suspend fun getEverything(filter: ArticlesFilter): Either<List<Article>, NewsError> =
        withContext(dispatchers.io) {
            kotlin.runCatching {

                val response: NewsApiResponse = httpClient.get((path("/v2/everything"))) {
                    parameter(LANGUAGE, filter.language)
                    parameter(
                        filter.query.isNotBlank(),
                        QUERY,
                        filter.query
                    )
                    parameter(
                        filter.sources.isNotEmpty(),
                        SOURCES,
                        filter.sources.joinToString(",") { it.id },
                    )
                    parameter(SORT_BY, filter.sortedBy.toNewsApiSortBy())
                    parameter(FROM_DATE, filter.fromDate.format(DateTimeFormatter.ISO_DATE))
                    parameter(TO_DATE, filter.toDate.format(DateTimeFormatter.ISO_DATE))
                }.body()

                when (response) {
                    is NewsApiArticles ->
                        response.articles.map(NewsApiArticle::toArticle).let(::Success)

                    else -> Either.Error(response.toError())
                }

            }.getOrElse { failure -> Either.error(failure.toError()) }

        }

    private fun path(path: String) = httpClientConfig.baseUrl + path

    private fun HttpRequestBuilder.parameter(
        condition: Boolean,
        key: String,
        value: Any?
    ) {
        if (condition) parameter(key, value)
    }

    companion object {

        const val CATEGORY = "category"

        const val QUERY = "q"
        const val SOURCES = "sources"
        const val LANGUAGE = "language"
        const val SORT_BY = "sortBy"
        const val FROM_DATE = "from"
        const val TO_DATE = "to"
    }
}

private fun SortBy.toNewsApiSortBy() = when (this) {
    SortBy.Relevancy -> "relevancy"
    SortBy.Popularity -> "popularity"
    SortBy.MostRecent -> "publishedAt"
}

private fun NewsApiSource.toArticleSource() = ArticlesSources(
    id = id ?: "",
    name = name,
    language = language ?: "en",
    category = category ?: "general",
)

private fun NewsApiArticle.toArticle() = Article(
    id = hashCode().toString(),
    source = source?.name ?: "",
    title = title,
    description = description ?: "",
    content = content ?: "",
    imageURL = urlToImage ?: "",
    contentUrl = url ?: "",
)

private fun Any.toError(): NewsError =
    when (this) {
        is NewsApiError -> {
            NewsError(code ?: "unKnownCode", message ?: "Unknown error api error")
        }

        is Throwable -> {
            NewsError("unKnownCode", message ?: "Unknown error api error")
        }

        else -> {
            NewsError("unKnownCode", "Unknown api error")
        }
    }
