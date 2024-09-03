package eu.acaraus.news.test.data.news

import eu.acaraus.news.di.DataDi
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.repositories.NewsApi
import eu.acaraus.shared.lib.Either
import eu.acaraus.shared.lib.onEachError
import eu.acaraus.shared.lib.onEachSuccess
import eu.acaraus.shared.test.lib.UnitTest
import eu.acaraus.shared.test.lib.rules.KoinUnitTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.ksp.generated.module
import org.koin.test.inject
import java.time.LocalDate

class NewsApiTest : UnitTest {

    @get:Rule
    override val koinUnitTestRule = KoinUnitTestRule(
        DataDi().module,
    )

    @Test
    fun fetch_news_headlines_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val headlinesResult: Either<List<Article>,NewsError> = newsApi.getHeadlines()
        assert(headlinesResult.isSuccess)
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

    @Test
    fun fetch_everything_news_invalid_parameter() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val filter = ArticlesFilter(
            query = "android",
            language = "de",
            fromDate = LocalDate.now().minusYears(29)
        )
      newsApi.getEverything(filter)
          .onEachError {
            assert(it.code == "parameterInvalid")
          }.onEachSuccess {
              assert(false) { "Should not be invoked" }
          }
    }
}
