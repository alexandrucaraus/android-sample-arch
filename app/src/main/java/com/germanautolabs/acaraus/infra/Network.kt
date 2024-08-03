package com.germanautolabs.acaraus.infra

import com.germanautolabs.acaraus.data.news.newsApiSerializationModule
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
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named("baseUrl")
fun baseUrl(): String = "https://newsapi.org/"

@Single
@Named("newsApiKey")
fun newsApiKey(): String = "89c24df786c04686b4130855488e1dd9"

@Single
fun client(
    json: Json,
    @Named("newsApiKey") newsApiKey: String,
): HttpClient =
    HttpClient(OkHttp) {
        if (IS_LOGGING_ENABLED) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
        install(ContentNegotiation) {
            json(json)
        }
//        install(HttpTimeout) {
//            socketTimeoutMillis = 60_000
//            connectTimeoutMillis = 60_000
//            requestTimeoutMillis = 60_000
//        }
        install(ContentEncoding) {
            deflate()
            gzip()
        }
        install(DefaultRequest) {
            header(HttpHeaders.Authorization, newsApiKey)
        }
//        install(HttpRequestRetry) {
//            maxRetries = 5
//            retryOnServerErrors(maxRetries = 5)
//
//            retryIf { _, response ->
//                !response.status.isSuccess()
//            }
//            retryOnExceptionIf { _, cause ->
//                cause is HttpRequestTimeoutException
//            }
//            retryOnExceptionIf { _, cause ->
//                cause is SocketTimeoutException
//            }
//
//            exponentialDelay()
//        }
    }

@Single
fun parser(): Json =
    Json {
        serializersModule = newsApiSerializationModule
        classDiscriminator = "status"
        ignoreUnknownKeys = true
        isLenient = true
    }

private const val IS_LOGGING_ENABLED = false
