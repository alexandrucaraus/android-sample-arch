package eu.acaraus.news.data.remote


import eu.acaraus.news.BuildConfig
import org.koin.core.annotation.Single

@Single
data class NewsApiConfig(
    val baseUrl: String = BuildConfig.NEWS_BASE_URL,
    val apiKey: String = BuildConfig.NEWS_API_KEY,
)
