package com.germanautolabs.acaraus.test.data.news

import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.main.DataDi
import com.germanautolabs.acaraus.main.InfraDi
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.test.main.rules.KoinUnitTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject

class NewsApiTest : KoinTest {

    @get:Rule
    val koinUnitTestRule = KoinUnitTestRule(
        listOf(InfraDi().module, DataDi().module),
    )

    @Test
    fun fetchNewsHeadlines() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val headlinesResult = newsApi.getHeadlines()
        assert(headlinesResult.isSuccess)
        assert(headlinesResult.success?.isNotEmpty() == true)
    }

    @Test
    fun fetchSources() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val sourcesResult = newsApi.getSources()
        assert(sourcesResult.isSuccess)
        assert(sourcesResult.success?.isNotEmpty() == true)
    }

    @Test
    fun fetchEverything() = runTest {
        val newsApi: NewsApi by inject<NewsApi>()
        val filter = ArticleFilter(
            query = "android",
            language = "de",
        )
        val everythingResult = newsApi.getEverything(filter)
        assert(everythingResult.isSuccess)
        assert(everythingResult.success?.isNotEmpty() == true)
    }
}
