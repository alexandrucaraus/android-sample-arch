package com.germanautolabs.acaraus.data.news

import com.germanautolabs.acaraus.infra.Dispatchers
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.Result.Success
import com.germanautolabs.acaraus.models.SortBy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import java.time.format.DateTimeFormatter

interface NewsApi {

    suspend fun getHeadlines(
        language: String = "de",
        category: String = "general",
    ): Result<List<Article>, Error>

    suspend fun getSources(): Result<List<ArticlesSources>, Error>

    suspend fun getEverything(filter: ArticlesFilter): Result<List<Article>, Error>
}

@Factory(binds = [NewsApi::class])
class NewsApiImpl(
    private val httpClient: HttpClient,
    @Named("baseUrl") private val baseUrl: String,
    private val dispatchers: Dispatchers,
) : NewsApi {

    override suspend fun getHeadlines(
        language: String,
        category: String,
    ): Result<List<Article>, Error> = withContext(dispatchers.io) {
        val response: NewsApiResponse = httpClient.get(path("/v2/top-headlines")) {
            parameter(LANGUAGE, language)
            parameter(CATEGORY, category)
        }.body()

        when (response) {
            is NewsApiArticles ->
                response
                    .articles
                    .map(NewsApiArticle::toArticle)
                    .let(::Success)

            else -> Result.Error(response.toError())
        }
    }

    override suspend fun getSources(): Result<List<ArticlesSources>, Error> =
        withContext(dispatchers.io) {
            val response: NewsApiResponse = httpClient.get(path("/v2/top-headlines/sources")).body()

            when (response) {
                is NewsApiSources ->
                    response
                        .sources
                        .map(NewsApiSource::toArticleSource)
                        .let(::Success)

                else -> Result.Error(response.toError())
            }
        }

    override suspend fun getEverything(filter: ArticlesFilter): Result<List<Article>, Error> =
        withContext(dispatchers.io) {
            val response: NewsApiResponse = httpClient.get((path("/v2/everything"))) {
                parameter(LANGUAGE, filter.language)
                if (filter.query.isNotBlank()) parameter(QUERY, filter.query)
                if (filter.sources.isNotEmpty()) parameter(
                    SOURCES, filter.sources.joinToString(",") { it.id }
                )
                parameter(SORT_BY, filter.sortedBy.toNewsApiSortBy())
                parameter(FROM_DATE, filter.fromDate.format(DateTimeFormatter.ISO_DATE))
                parameter(TO_DATE, filter.toDate.format(DateTimeFormatter.ISO_DATE))
            }.body()
            when (response) {
                is NewsApiArticles ->
                    response
                        .articles
                        .map(NewsApiArticle::toArticle)
                        .let(::Success)

                else -> Result.Error(response.toError())
            }
        }

    private fun path(path: String) = baseUrl + path

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

private fun NewsApiResponse.toError(): Error =
    if (this is NewsApiError) {
        Error(code ?: "unKnownCode", message ?: "Unknown error api error")
    } else {
        Error("unKnownCode", "Unknown api error")
    }
