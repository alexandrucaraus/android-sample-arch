package com.germanautolabs.acaraus.screens.articles.list

import androidx.lifecycle.ViewModel
import com.germanautolabs.acaraus.models.Article
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticleListViewModel : ViewModel() {

    private val defaultArticle = Article(
        id = "0",
        title = "Old news",
        description = "Everything new is a well-forgotten old",
        source = "Internet",
        imageURL = null,
    )

    val list = listOf(
        defaultArticle,
        defaultArticle.copy(id = "1", title = "New news 1"),
        defaultArticle.copy(id = "2", title = "New news 2"),
        defaultArticle.copy(id = "3", title = "New news 3"),
        defaultArticle.copy(id = "4", title = "New news 4"),
    )
}
