package eu.acaraus.news.data.remote

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
)

// Error
@Serializable
data class NewsApiError(
    val code: String?,
    val message: String?,
) : NewsApiResponse()

// Serializer
object NewsApiResponseSerializer :
    JsonContentPolymorphicSerializer<NewsApiResponse>(NewsApiResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<NewsApiResponse> {
        return when (element.jsonObject["status"]?.jsonPrimitive?.content) {
            "ok" -> objectDeserializer(element)
            "error" -> NewsApiError.serializer()
            else -> throw SerializationException("Unknown status for NewsApiResponse")
        }
    }

    private fun objectDeserializer(element: JsonElement) = when {
        element.jsonObject.containsKey("articles") -> NewsApiArticles.serializer()
        element.jsonObject.containsKey("sources") -> NewsApiSources.serializer()
        else -> throw SerializationException("Unknown status for NewsApiResponse")
    }

    val module = SerializersModule {
        polymorphic(NewsApiResponse::class) {
            subclass(NewsApiArticles::class)
            subclass(NewsApiSources::class)
            subclass(NewsApiError::class)
        }
    }
}
