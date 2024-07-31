package com.germanautolabs.acaraus.models

data class Article(
    val id: String,
    val source: String,
    val title: String,
    val description: String?,
    val imageURL: String?,
)

// todo remove
private val dummyArticle = Article(
    id = "0",
    title = "Old news",
    description = "Everything new is a well-forgotten old",
    source = "Internet",
    imageURL = null,
)

val dummyArticleList = listOf(
    dummyArticle,
    dummyArticle.copy(id = "1", title = "New news 1"),
    dummyArticle.copy(id = "2", title = "New news 2"),
    dummyArticle.copy(id = "3", title = "New news 3"),
    dummyArticle.copy(id = "4", title = "New news 4"),
)
