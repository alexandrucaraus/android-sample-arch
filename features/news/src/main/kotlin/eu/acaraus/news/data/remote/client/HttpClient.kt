package eu.acaraus.news.data.remote.client

import eu.acaraus.news.data.remote.newsApiSerializationModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
fun client(
    json: Json,
    httpClientConfig: HttpClientConfig
): HttpClient =
    HttpClient(OkHttp) {
        install(DefaultRequest) {
            header(HttpHeaders.Authorization, httpClientConfig.newsApiKey)
        }
        if (IS_LOGGING_ENABLED) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(ContentEncoding) {
            deflate()
            gzip()
        }
    }

@Single
fun parser(): Json =
    Json {
        serializersModule = newsApiSerializationModule
        ignoreUnknownKeys = true
        isLenient = true
    }

private const val IS_LOGGING_ENABLED = false
