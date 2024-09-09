package eu.acaraus.news.test.data.news

import eu.acaraus.core.Either
import eu.acaraus.core.onEachError
import eu.acaraus.core.onEachSuccess
import eu.acaraus.news.data.remote.NewsApiConfig
import eu.acaraus.news.di.DataDi
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.repositories.NewsRepository
import eu.acaraus.news.test.rules.UTest
import eu.acaraus.shared.lib.http.httpClient
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.inject
import java.time.LocalDate

class NewsRepositoryTest : UTest {

    override fun perTestModules(): Array<Module> = arrayOf(
        DataDi().module,
        module { single { httpClient() } },
        module {
            single {
                NewsApiConfig(
                    baseUrl = "https://newsapi.org/",
                    apiKey = "a6d3cd2d5932471db7c7d8e68628bc5e",
                )
            }
        },
    )

    @Test
    fun fetch_news_headlines_successfully() = runTest {
        val newsRepository: NewsRepository by inject<NewsRepository>()
        val headlinesResult = newsRepository.getHeadlines()
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
