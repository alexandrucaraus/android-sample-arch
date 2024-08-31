package eu.acaraus.news.data.remote

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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
}

val newsApiSerializationModule = SerializersModule {
    polymorphic(NewsApiResponse::class) {
        subclass(NewsApiArticles::class)
        subclass(NewsApiSources::class)
        subclass(NewsApiError::class)
    }
}
