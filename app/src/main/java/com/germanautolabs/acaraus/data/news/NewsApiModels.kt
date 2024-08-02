package com.germanautolabs.acaraus.data.news

import kotlinx.serialization.Serializable

@Serializable(with = NewsApiResponseSerializer::class)
abstract class NewsApiResponse

// Articles
@Serializable
data class NewsApiArticles(
    val articles: List<NewsApiArticle>,
    val totalResults: Int,
) : NewsApiResponse()

@Serializable
data class NewsApiArticle(
    val source: NewsApiArticleSource?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
)

@Serializable
data class NewsApiArticleSource(
    val id: String?,
    val name: String?,
)

// Sources
@Serializable
data class NewsApiSources(
    val sources: List<NewsApiSource>,
) : NewsApiResponse()

@Serializable
data class NewsApiSource(
    val id: String?,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
)

// Error
@Serializable
data class NewsApiError(
    val code: String?,
    val message: String?,
) : NewsApiResponse()
