package com.germanautolabs.acaraus.models

data class Article(
    val id: String,
    val source: String,
    val title: String,
    val description: String?,
    val content: String,
    val imageURL: String?,
    val contentUrl: String,
)

// todo remove
private val dummyArticle = Article(
    id = "0",
    title = "Old news",
    description = "Everything new is a well-forgotten old",
    source = "Internet",
    imageURL = null,
    content = "",
    contentUrl = "https://www.wired.com/story/bitcoin-bros-go-wild-for-donald-trump/",
)

val dummyArticleList = listOf(
    dummyArticle,
    dummyArticle.copy(id = "1", title = "New news 1"),
    dummyArticle.copy(id = "2", title = "New news 2"),
    dummyArticle.copy(id = "3", title = "New news 3"),
    dummyArticle.copy(id = "4", title = "New news 4"),
)
