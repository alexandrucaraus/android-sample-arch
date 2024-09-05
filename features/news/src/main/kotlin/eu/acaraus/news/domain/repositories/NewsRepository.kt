package eu.acaraus.news.domain.repositories

import eu.acaraus.core.Either
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.NewsError

interface NewsRepository {

    suspend fun getHeadlines(
        language: String = "de",
        category: String = "general",
    ): Either<NewsError, List<Article>>

    suspend fun getSources(): Either<NewsError, List<ArticlesSources>>

    suspend fun getEverything(filter: ArticlesFilter): Either<NewsError, List<Article>>
}
