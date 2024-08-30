package eu.acaraus.news.domain.repositories

import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.Either
import eu.acaraus.news.domain.entities.NewsError

interface NewsApi {

    suspend fun getHeadlines(
        language: String = "de",
        category: String = "general",
    ): Either<List<Article>, NewsError>

    suspend fun getSources(): Either<List<ArticlesSources>, NewsError>

    suspend fun getEverything(filter: ArticlesFilter): Either<List<Article>, NewsError>
}
