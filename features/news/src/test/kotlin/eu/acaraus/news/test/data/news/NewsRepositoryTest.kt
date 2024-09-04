package eu.acaraus.news.test.data.news

import eu.acaraus.news.di.DataDi
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.repositories.NewsRepository
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

class NewsRepositoryTest : UnitTest {

    @get:Rule
    override val koinUnitTestRule = KoinUnitTestRule(
        DataDi().module,
    )

    @Test
    fun fetch_news_headlines_successfully() = runTest {
        val newsRepository: NewsRepository by inject<NewsRepository>()
        val headlinesResult: Either<List<Article>, NewsError> = newsRepository.getHeadlines()
        assert(headlinesResult.isSuccess)
    }

    @Test
    fun fetch_news_sources_successfully() = runTest {
        val newsApi: NewsRepository by inject<NewsRepository>()
        val sourcesResult = newsApi.getSources()
        assert((sourcesResult as Either.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_everything_news_successfully() = runTest {
        val newsRepository: NewsRepository by inject<NewsRepository>()
        val filter = ArticlesFilter(
            query = "android",
            language = "de",
        )
        val everythingResult = newsRepository.getEverything(filter)
        assert((everythingResult as Either.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_everything_news_invalid_parameter() = runTest {
        val newsRepository: NewsRepository by inject<NewsRepository>()
        val filter = ArticlesFilter(
            query = "android",
            language = "de",
            fromDate = LocalDate.now().minusYears(29),
        )
        newsRepository.getEverything(filter)
            .onEachError {
                assert(it.code == "parameterInvalid")
            }.onEachSuccess {
                assert(false) { "Should not be invoked" }
            }
    }
}
