package com.germanautolabs.acaraus.test.screens.articles.list

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.LocaleStoreImpl
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticlesFilter
import com.germanautolabs.acaraus.models.ArticlesSources
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.articles.list.holders.ArticlesFilterStateHolder
import com.germanautolabs.acaraus.test.main.rules.CoroutinesTestRule
import com.germanautolabs.acaraus.test.main.rules.KoinUnitTestRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.LocalDate
import kotlin.test.assertEquals

class ArticlesFilterStateHolderTest : KoinTest {

    @get:Rule
    val koinUnitTestRule = KoinUnitTestRule(listOf(ArticlesFilterStateHolderModule().module))

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private fun createSubject(coroutineScope: CoroutineScope): ArticlesFilterStateHolder =
        inject<ArticlesFilterStateHolder> { parametersOf(coroutineScope) }.value

    @Test
    fun check_that_article_sources_are_loaded() = runTest {
        turbineScope {
            val filterStateHolder = createSubject(backgroundScope)
            filterStateHolder.articlesFilterUiState.test {
                skipItems(2)
                val filterEditorState = awaitItem()
                assertEquals(
                    actual = filterEditorState.sourceOptions.count(),
                    expected = dummyArticlesSources.count { it.language == "en" } + 1,
                ) // +1 for "All"
            }
        }
    }

    @Test
    fun select_Topics_SortBy_Language_Sources_And_Apply_Filter() = runTest {
        turbineScope {
            val filterStateHolder = createSubject(backgroundScope)
            filterStateHolder.articlesFilterUiState.test {
                skipItems(2)
                var state = awaitItem()
                state.setQuery("android")
                state = awaitItem()
                state.setSortBy(SortBy.Relevancy.name)
                state = awaitItem()
                state.setLanguage("German")
                state = awaitItem()
                state.setSource("Der Spiegel")
                state = awaitItem()
                state.setFromDate(LocalDate.of(2022, 1, 1))
                state = awaitItem()
                state.setToDate(LocalDate.of(2022, 12, 31))
                state = awaitItem()

                state.apply()

                assertEquals(true, state.sourceOptions.isNotEmpty())
                assertEquals("Relevancy", state.sortBy)
                assertEquals("android", state.query)
                assertEquals("German", state.language)
                assertEquals("Der Spiegel", state.source)
                assertEquals(LocalDate.of(2022, 1, 1), state.fromOldestDate)
                assertEquals(LocalDate.of(2022, 12, 31), state.toNewestDate)

                filterStateHolder.articlesFilterState.test {
                    val currentFilter = awaitItem()
                    assertEquals("android", currentFilter.query)
                    assertEquals("de", currentFilter.language)
                    assertEquals(SortBy.Relevancy, currentFilter.sortedBy)
                    assertEquals("Der Spiegel", currentFilter.sources.first().name)
                }
            }
        }
    }

    @Test
    fun select_Topics_SortBy_Language_Sources_And_Reset_Filter() = runTest {
        turbineScope {
            val filterStateHolder = createSubject(backgroundScope)
            filterStateHolder.articlesFilterUiState.test {
                skipItems(2)
                var state = awaitItem()
                state.setQuery("android")
                state = awaitItem()
                state.setSortBy(SortBy.Relevancy.name)
                state = awaitItem()
                state.setLanguage("German")
                state = awaitItem()
                state.setSource("Der Spiegel")
                state = awaitItem()
                state.setFromDate(LocalDate.of(2022, 1, 1))
                state = awaitItem()
                state.setToDate(LocalDate.of(2022, 12, 31))
                state = awaitItem()

                assertEquals(true, state.sourceOptions.isNotEmpty())
                assertEquals("Relevancy", state.sortBy)
                assertEquals("android", state.query)
                assertEquals("German", state.language)
                assertEquals("Der Spiegel", state.source)
                assertEquals(LocalDate.of(2022, 1, 1), state.fromOldestDate)
                assertEquals(LocalDate.of(2022, 12, 31), state.toNewestDate)

                state.reset()
                state = awaitItem()

                assertEquals(true, state.sourceOptions.isNotEmpty())
                assertEquals("MostRecent", state.sortBy)
                assertEquals("", state.query)
                assertEquals("All", state.source)
                assertEquals("German", state.language)

                filterStateHolder.articlesFilterState.test {
                    val currentFilter = awaitItem()
                    assertEquals("", currentFilter.query)
                    assertEquals("de", currentFilter.language)
                    assertEquals(SortBy.MostRecent, currentFilter.sortedBy)
                    assertEquals(emptyList(), currentFilter.sources)
                }
            }
        }
    }
}

@Module
class ArticlesFilterStateHolderModule {

    @Factory
    fun newsApi(): NewsApi = object : NewsApi {
        override suspend fun getHeadlines(
            language: String,
            category: String,
        ): Result<List<Article>, Error> {
            TODO("Not yet implemented")
        }

        override suspend fun getSources(): Result<List<ArticlesSources>, Error> {
            return Result.Success(dummyArticlesSources)
        }

        override suspend fun getEverything(filter: ArticlesFilter): Result<List<Article>, Error> {
            TODO("Not yet implemented")
        }
    }

    @Single
    fun localeStore(): LocaleStore = LocaleStoreImpl()
}

val dummyArticlesSources = listOf(
    ArticlesSources(
        id = "techcrunch",
        name = "TechCrunch",
        language = "en",
        category = "Technology",
    ),
    ArticlesSources(
        id = "the-verge",
        name = "The Verge",
        language = "en",
        category = "Technology",
    ),
    ArticlesSources(
        id = "wired",
        name = "Wired",
        language = "en",
        category = "Technology",
    ),
    ArticlesSources(
        id = "bloomberg",
        name = "Bloomberg",
        language = "en",
        category = "Business",
    ),
    ArticlesSources(
        id = "national-geographic",
        name = "National Geographic",
        language = "en",
        category = "Science",
    ),
    ArticlesSources(
        id = "bbc-news",
        name = "BBC News",
        language = "en",
        category = "General",
    ),
    ArticlesSources(
        id = "le-monde",
        name = "Le Monde",
        language = "fr",
        category = "General",
    ),
    ArticlesSources(
        id = "der-spiegel",
        name = "Der Spiegel",
        language = "de",
        category = "General",
    ),
    ArticlesSources(
        id = "el-pais",
        name = "El País",
        language = "es",
        category = "General",
    ),
)
