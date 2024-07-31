package com.germanautolabs.acaraus.models

data class Article(
    val id: String,
    val source: String,
    val title: String,
    val description: String?,
    val imageURL: String?,
)
