package com.germanautolabs.acaraus.data

import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.dummyArticleList
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
