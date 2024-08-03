package com.germanautolabs.acaraus.models

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val source: String,
    val title: String,
    val description: String?,
    val content: String,
    val imageURL: String?,
    val contentUrl: String,
)
