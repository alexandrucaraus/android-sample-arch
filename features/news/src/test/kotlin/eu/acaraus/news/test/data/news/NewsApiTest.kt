package eu.acaraus.news.test.data.news

import eu.acaraus.news.di.DataDi
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.repositories.NewsApi
import eu.acaraus.news.test.common.UnitTest
import eu.acaraus.news.test.common.rules.KoinUnitTestRule
import eu.acaraus.shared.lib.Either
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.ksp.generated.module
import org.koin.test.inject

class NewsApiTest : UnitTest {

    @get:Rule
    override val koinUnitTestRule = KoinUnitTestRule(
        DataDi().module,
    )

    @Test
    fun fetch_news_headlines_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val headlinesResult = newsApi.getHeadlines()
        assert((headlinesResult as Either.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_news_sources_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val sourcesResult = newsApi.getSources()
        assert((sourcesResult as Either.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_everything_news_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val filter = ArticlesFilter(
            query = "android",
            language = "de",
        )
        val everythingResult = newsApi.getEverything(filter)
        assert((everythingResult as Either.Success).value.isNotEmpty())
    }
}
