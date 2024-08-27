package com.germanautolabs.acaraus.test.data.news

import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.main.DataDi
import com.germanautolabs.acaraus.main.InfraDi
import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.test.common.UnitTest
import com.germanautolabs.acaraus.test.common.rules.KoinUnitTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.ksp.generated.module
import org.koin.test.inject

class NewsApiTest : UnitTest {

    @get:Rule
    override val koinUnitTestRule = KoinUnitTestRule(
        InfraDi().module,
        DataDi().module,
    )

    @Test
    fun fetch_news_headlines_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val headlinesResult = newsApi.getHeadlines()
        assert((headlinesResult as Result.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_news_sources_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val sourcesResult = newsApi.getSources()
        assert((sourcesResult as Result.Success).value.isNotEmpty())
    }

    @Test
    fun fetch_everything_news_successfully() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val filter = ArticlesFilter(
            query = "android",
            language = "de",
        )
        val everythingResult = newsApi.getEverything(filter)
        assert((everythingResult as Result.Success).value.isNotEmpty())
    }
}
