package com.germanautolabs.acaraus.test.screens.articles.list

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.germanautolabs.acaraus.data.LocaleStore
import com.germanautolabs.acaraus.data.LocaleStoreImpl
import com.germanautolabs.acaraus.data.news.NewsApi
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.models.ArticleFilter
import com.germanautolabs.acaraus.models.ArticleSource
import com.germanautolabs.acaraus.models.Error
import com.germanautolabs.acaraus.models.Result
import com.germanautolabs.acaraus.models.SortBy
import com.germanautolabs.acaraus.screens.articles.list.ArticleFilterStateHolder
import com.germanautolabs.acaraus.test.main.rules.CoroutinesTestRule
import com.germanautolabs.acaraus.test.main.rules.KoinUnitTestRule
import com.germanautolabs.acaraus.usecase.GetArticleSources
import com.germanautolabs.acaraus.usecase.GetArticlesLanguages
import com.germanautolabs.acaraus.usecase.GetLocale
import com.germanautolabs.acaraus.usecase.SetLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class ArticleFilterStateHolderTest : KoinTest {

    @get:Rule
    val koinUnitTestRule = KoinUnitTestRule(listOf(Test1Fakes().module))

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private fun createSubject(coroutineScope: CoroutineScope): ArticleFilterStateHolder {
        val getArticleSources by inject<GetArticleSources>()
        val setLocale by inject<SetLocale>()
        val getLocale by inject<GetLocale>()
        val newsLanguage by inject<GetArticlesLanguages>()

        return ArticleFilterStateHolder(
            getArticleSources = getArticleSources,
            setLocale = setLocale,
            getLocale = getLocale,
            newsLanguage = newsLanguage,
            currentScope = coroutineScope,
        )
    }

    @Test
    fun check_that_article_sources_are_loaded() = runTest {
        val filterStateHolder = createSubject(this)
        turbineScope {
            filterStateHolder.filterEditorState.test {
                skipItems(1)
                val filterEditorState = awaitItem()
                assertEquals(
                    actual = filterEditorState.sourceOptions.count(),
                    expected = dummyArticleSources.count { it.language == "en" } + 1,
                ) // +1 for "All"
            }
        }
    }

    @Test
    fun select_Topics_SortBy_Language_Sources_And_Apply_Filter() = runTest {
        val filterStateHolder = createSubject(this)
        turbineScope {
            filterStateHolder.filterEditorState.test {
                skipItems(1)
                val state = awaitItem()
                assertEquals(state.sourceOptions.isNotEmpty(), true)
                state.setQuery("android")
                assert(awaitItem().query == "android")
                state.setSortBy(SortBy.Relevancy.name)
                assert(awaitItem().sortBy == SortBy.Relevancy.name)
                state.setLanguage("German")
                assert(awaitItem().language == "German")
                state.setSource("Der Spiegel")
                assert(awaitItem().source == "Der Spiegel")
                state.apply()

                filterStateHolder.currentFilter.test {
                    val currentFilter = awaitItem()
                    assert(currentFilter.query == "android")
                    assert(currentFilter.language == "de")
                    assert(currentFilter.sortedBy == SortBy.Relevancy)
                    assert(currentFilter.sources.first().name == "Der Spiegel")
                }
            }
        }
    }
}

@Module
class Test1Fakes {

    @Factory
    fun newsApi(): NewsApi = NetworkApi()

    @Single
    fun localeStore(): LocaleStore = LocaleStoreImpl()
}

class NetworkApi : NewsApi {

    override suspend fun getHeadlines(
        language: String,
        category: String,
    ): Result<List<Article>, Error> {
        TODO("Not implemented")
    }

    override suspend fun getSources(
        language: String,
        category: String,
    ): Result<List<ArticleSource>, Error> {
        return Result.success(dummyArticleSources)
    }

    override suspend fun getEverything(filter: ArticleFilter): Result<List<Article>, Error> {
        TODO("Not implemented")
    }
}

val dummyArticleSources = listOf(
    ArticleSource(
        id = "techcrunch",
        name = "TechCrunch",
        language = "en",
        category = "Technology",
    ),
    ArticleSource(
        id = "the-verge",
        name = "The Verge",
        language = "en",
        category = "Technology",
    ),
    ArticleSource(
        id = "wired",
        name = "Wired",
        language = "en",
        category = "Technology",
    ),
    ArticleSource(
        id = "bloomberg",
        name = "Bloomberg",
        language = "en",
        category = "Business",
    ),
    ArticleSource(
        id = "national-geographic",
        name = "National Geographic",
        language = "en",
        category = "Science",
    ),
    ArticleSource(
        id = "bbc-news",
        name = "BBC News",
        language = "en",
        category = "General",
    ),
    ArticleSource(
        id = "le-monde",
        name = "Le Monde",
        language = "fr",
        category = "General",
    ),
    ArticleSource(
        id = "der-spiegel",
        name = "Der Spiegel",
        language = "de",
        category = "General",
    ),
    ArticleSource(
        id = "el-pais",
        name = "El País",
        language = "es",
        category = "General",
    ),
)
