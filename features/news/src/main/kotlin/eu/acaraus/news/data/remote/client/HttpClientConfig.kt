package eu.acaraus.news.data.remote.client

import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("baseUrl")
fun baseUrl(): String = "https://newsapi.org/"

@Single
@Named("newsApiKey")
fun newsApiKey(): String = "a6d3cd2d5932471db7c7d8e68628bc5e"

@Single
data class HttpClientConfig(
    @Named("baseUrl") val baseUrl: String,
    @Named("newsApiKey") val newsApiKey: String,
)
