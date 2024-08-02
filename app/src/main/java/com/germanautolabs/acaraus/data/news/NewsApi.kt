package com.germanautolabs.acaraus.data.news

import com.germanautolabs.acaraus.infra.Dispatchers
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import java.time.format.DateTimeFormatter

interface NewsApi {

    suspend fun getHeadlines(
        language: String = "de",
        country: String = "de",
    ): Result<List<Article>, Error>

    suspend fun getSources(): Result<List<ArticleSource>, Error>

    suspend fun getEverything(filter: ArticleFilter): Result<List<Article>, Error>
}

@Factory(binds = [NewsApi::class])
class NewsApiImpl(
    private val httpClient: HttpClient,
    @Named("baseUrl") private val baseUrl: String,
    @Named("newsApiKey") private val newsApiKey: String,
    private val dispatchers: Dispatchers,
) : NewsApi {

    init {
        httpClient.sendPipeline.intercept(HttpSendPipeline.State) {
            context.headers.append(HttpHeaders.Authorization, newsApiKey)
        }
    }

    override suspend fun getHeadlines(
        language: String,
        country: String,
    ): Result<List<Article>, Error> = withContext(dispatchers.io) {
        val response: NewsApiResponse = httpClient.get(path("/v2/top-headlines")) {
            parameter(LANGUAGE, language)
            parameter(COUNTRY, country)
        }.body()

        when (response) {
            is NewsApiArticles -> response.articles.map(NewsApiArticle::toArticle)
                .let { articles -> Result.success(articles) }

            is NewsApiError -> Result.error(response.toError())

            else -> Result.error(Error("parserError", "No error message"))
        }
    }

    override suspend fun getSources(): Result<List<ArticleSource>, Error> =
        withContext(dispatchers.io) {
            val response: NewsApiResponse = httpClient.get(path("/v2/top-headlines/sources")).body()

            when (response) {
                is NewsApiSources -> response.sources.map(NewsApiSource::toArticleSource)
                    .let { Result.success(it) }

                is NewsApiError -> Result.error(response.toError())

                else -> Result.error(Error("parseError", "No error message"))
            }
        }

    override suspend fun getEverything(filter: ArticleFilter): Result<List<Article>, Error> =
        withContext(dispatchers.io) {
            val response: NewsApiResponse = httpClient.get((path("/v2/everything"))) {
                parameter(LANGUAGE, filter.language)
                if (filter.query.isNotBlank()) {
                    parameter(QUERY, filter.query)
                }
                if (filter.sources.isNotEmpty()) {
                    parameter(SOURCES, filter.sources.joinToString(","))
                }
                if (filter.sources.isNotEmpty()) {
                    parameter(SORT_BY, filter.sortedBy.toNewsApiSortBy())
                }
                parameter(FROM_DATE, filter.fromDate.format(DateTimeFormatter.ISO_DATE))
                parameter(TO_DATE, filter.toDate.format(DateTimeFormatter.ISO_DATE))
            }.body()
            when (response) {
                is NewsApiArticles -> response.articles.map(NewsApiArticle::toArticle)
                    .let { Result.success(it) }

                is NewsApiError -> Result.error(response.toError())

                else -> Result.error(Error("parseError", "No error message"))
            }
        }

    private fun path(path: String) = baseUrl + path

    companion object {

        const val COUNTRY = "country"

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

private fun NewsApiSource.toArticleSource() = ArticleSource(name = name)

private fun NewsApiArticle.toArticle() = Article(
    id = hashCode().toString(),
    source = source?.name ?: "",
    title = title,
    description = description ?: "",
    content = content ?: "",
    imageURL = urlToImage ?: "",
    contentUrl = url ?: "",
)

private fun NewsApiError.toError(): Error =
    Error(code ?: "unKnownCode", message ?: "No error message")
