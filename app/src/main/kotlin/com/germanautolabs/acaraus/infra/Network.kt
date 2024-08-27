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
fun newsApiKey(): String = "a6d3cd2d5932471db7c7d8e68628bc5e" // "89c24df786c04686b4130855488e1dd9"

@Single
fun client(
    json: Json,
    @Named("newsApiKey") newsApiKey: String,
): HttpClient =
    HttpClient(OkHttp) {
        install(DefaultRequest) {
            header(HttpHeaders.Authorization, newsApiKey)
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
