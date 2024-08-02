package com.germanautolabs.acaraus.data

import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.Result
import org.koin.core.annotation.Factory

@Factory
class ArticleRepository {

    suspend fun get(param: Params): Result<List<Article>, String> {
        return Result.success(dummyArticleList)
    }

    data class Params(
        val filter: ArticleFilter,
    )
}

// todo remove
private val dummyArticle = Article(
    id = "0",
    title = "Old news",
    description = "Everything new is a well-forgotten old",
    source = "Internet",
    imageURL = "https://picsum.photos/200",
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

