package eu.acaraus.news.data.remote

import eu.acaraus.core.Either
import eu.acaraus.core.Either.Error
import eu.acaraus.core.Either.Success
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.SortBy
import eu.acaraus.news.domain.repositories.NewsRepository
import eu.acaraus.shared.lib.coroutines.DispatcherProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import java.time.format.DateTimeFormatter

@Factory(binds = [NewsRepository::class])
internal class NewsApi(
    httpClientInstance: HttpClient,
    private val newsApiConfig: NewsApiConfig,
    private val dispatchers: DispatcherProvider,
) : NewsRepository {

    private val httpClient by lazy {
        httpClientInstance.config {
            install(DefaultRequest) {
                header(HttpHeaders.Authorization, newsApiConfig.apiKey)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        serializersModule = NewsApiResponseSerializer.module
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    override suspend fun getHeadlines(
        language: String,
        category: String,
    ): Either<NewsError, List<Article>> = withContext(dispatchers.io) {
        kotlin.runCatching {
            httpClient.get(pathOf(HEADLINES_ENDPOINT)) {
                parameter(LANGUAGE, language)
                parameter(CATEGORY, category)
            }.body<NewsApiResponse>().let { response ->
                if (response is NewsApiArticles) {
                    response.articles.map(NewsApiArticle::toArticle).let(::Success)
                } else {
                    response.toNewsError().let(::Error)
                }
            }
        }.getOrElse { failure -> failure.toNewsError().let(::Error) }
    }

    override suspend fun getSources(): Either<NewsError, List<ArticlesSources>> =
        withContext(dispatchers.io) {
            kotlin.runCatching {
                httpClient.get(pathOf(SOURCES_ENDPOINT))
                    .body<NewsApiResponse>().let { response ->
                        if (response is NewsApiSources) {
                            response.sources.map(NewsApiSource::toArticleSource).let(::Success)
                        } else {
                            response.toNewsError().let(::Error)
                        }
                    }
            }.getOrElse { failure -> failure.toNewsError().let(::Error) }
        }

    override suspend fun getEverything(filter: ArticlesFilter): Either<NewsError, List<Article>> =
        withContext(dispatchers.io) {
            kotlin.runCatching {
                httpClient.get(pathOf(EVERYTHING_ENDPOINT)) {
                    parameter(LANGUAGE, filter.language)
                    conditionalParameter(
                        filter.query.isNotBlank(),
                        QUERY,
                        filter.query,
                    )
                    conditionalParameter(
                        filter.sources.isNotEmpty(),
                        SOURCES,
                        filter.sources.joinToString(",") { source -> source.id },
                    )
                    parameter(SORT_BY, filter.sortedBy.toNewsApiSortBy())
                    parameter(FROM_DATE, filter.fromDate.format(DateTimeFormatter.ISO_DATE))
                    parameter(TO_DATE, filter.toDate.format(DateTimeFormatter.ISO_DATE))
                }.body<NewsApiResponse>().let { response ->
                    if (response is NewsApiArticles) {
                        response.articles.map(NewsApiArticle::toArticle).let(::Success)
                    } else {
                        response.toNewsError().let(::Error)
                    }
                }
            }.getOrElse { failure -> failure.toNewsError().let(::Error) }
        }

    private fun pathOf(endpoint: String) = newsApiConfig.baseUrl + endpoint

    private fun HttpRequestBuilder.conditionalParameter(
        condition: Boolean,
        key: String,
        value: Any?,
    ) {
        if (condition) parameter(key, value)
    }

    companion object {

        const val HEADLINES_ENDPOINT = "/v2/top-headlines"
        const val CATEGORY = "category"

        const val SOURCES_ENDPOINT = "/v2/top-headlines/sources"

        const val EVERYTHING_ENDPOINT = "/v2/everything"
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

private fun Any.toNewsError(): NewsError =
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
