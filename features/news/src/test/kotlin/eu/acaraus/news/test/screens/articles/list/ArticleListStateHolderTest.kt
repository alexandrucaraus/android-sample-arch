package eu.acaraus.news.test.screens.articles.list

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import eu.acaraus.core.Either
import eu.acaraus.news.data.LocaleRepositoryImpl
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.news.domain.entities.ArticlesFilter
import eu.acaraus.news.domain.entities.ArticlesSources
import eu.acaraus.news.domain.entities.NewsError
import eu.acaraus.news.domain.entities.SortBy
import eu.acaraus.news.domain.repositories.LocaleRepository
import eu.acaraus.news.domain.repositories.NewsRepository
import eu.acaraus.news.presentation.list.holders.ArticlesListKoinScope
import eu.acaraus.news.presentation.list.holders.ArticlesListStateHolder
import eu.acaraus.news.test.rules.UTest
import eu.acaraus.shared.test.lib.di.injectScoped
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

class ArticleListStateHolderTest : UTest {

    override fun perTestModules() = arrayOf(ArticleListTestModule().module)

    private fun createSubject(coroutineScope: CoroutineScope) =
        injectScoped<ArticlesListStateHolder, ArticlesListKoinScope>(
            coroutineScope = coroutineScope,
        ).first

    @After
    fun teardown() {
        simulateApiError = false
    }

    @Test
    fun check_that_headline_articles_are_loaded() = runTest {
        turbineScope {
            val sourcesStateHolder = createSubject(backgroundScope)
            sourcesStateHolder.listUi.test {
                var state = awaitItem()
                delay(10)
                state.reload()
                state = awaitItem()
                assertEquals(true, state.isLoading)
                assertEquals(false, state.list.isNotEmpty())
                state = awaitItem()
                assertEquals(false, state.isLoading)
                assertEquals(true, state.list.isNotEmpty())
                assertEquals(dummyArticles.take(3), state.list)
            }
        }
    }

    @Test
    fun check_that_filtered_articles_are_loaded() = runTest {
        turbineScope {
            val sourcesStateHolder = createSubject(backgroundScope)
            sourcesStateHolder.listUi.test {
                skipItems(1)
                delay(10)
                sourcesStateHolder.reloadArticles(
                    articlesFilter = ArticlesFilter(
                        query = "android",
                        sortedBy = SortBy.Relevancy,
                        sources = emptyList(),
                    ),
                )
                var state = awaitItem()
                assertEquals(true, state.isLoading)
                assertEquals(false, state.list.isNotEmpty())
                state = awaitItem()
                assertEquals(false, state.isLoading)
                assertEquals(true, state.list.isNotEmpty())
                assertEquals(dummyArticles.takeLast(2), state.list)
            }
        }
    }

    @Test
    fun check_that_filtered_articles_are_failed() = runTest {
        turbineScope {
            simulateApiError = true
            val sourcesStateHolder = createSubject(backgroundScope)
            sourcesStateHolder.listUi.test {
                skipItems(1)
                delay(10)
                sourcesStateHolder.reloadArticles(
                    articlesFilter = ArticlesFilter(
                        query = "android",
                        sortedBy = SortBy.Relevancy,
                        sources = emptyList(),
                    ),
                )
                var state = awaitItem()
                assertEquals(true, state.isLoading)
                assertEquals(false, state.list.isNotEmpty())
                assertEquals(false, state.isError)
                state = awaitItem()
                assertEquals(false, state.isLoading)
                assertEquals(false, state.list.isNotEmpty())
                assertEquals(true, state.isError)
                assertEquals("Simulated Api Error", state.errorMessage)
            }
        }
    }
}

private var simulateApiError = false

@Module
class ArticleListTestModule {

    @Factory
    fun newsApi(): NewsRepository = object : NewsRepository {

        override suspend fun getHeadlines(
            language: String,
            category: String,
        ): Either<NewsError, List<Article>> = if (simulateApiError.not()) {
            Either.success(dummyArticles.take(3))
        } else {
            throw Exception("Simulated Api Error")
        }

        override suspend fun getSources(): Either<NewsError, List<ArticlesSources>> {
            TODO("Not implemented")
        }

        override suspend fun getEverything(
            filter: ArticlesFilter,
        ): Either<NewsError, List<Article>> = if (simulateApiError.not()) {
            Either.success(dummyArticles.takeLast(2))
        } else {
            throw Exception("Simulated Api Error")
        }
    }

    @Single
    fun localeStore(): LocaleRepository = LocaleRepositoryImpl()
}

private val dummyArticles = listOf(
    Article(
        id = "1",
        source = "TechCrunch",
        title = "Latest Innovations in AI",
        description = "An overview of the latest advancements in artificial intelligence technology.",
        content = "Artificial intelligence is transforming various sectors. The latest innovations include improvements in natural language processing, computer vision, and more.",
        imageURL = "https://example.com/images/ai-innovations.jpg",
        contentUrl = "https://techcrunch.com/latest-innovations-in-ai",
    ),
    Article(
        id = "2",
        source = "The Verge",
        title = "New Smartphone Releases",
        description = "A roundup of the newest smartphones hitting the market this year.",
        content = "This year has seen some exciting new smartphone releases, with advanced features and cutting-edge technology. Here’s a look at the top releases.",
        imageURL = "https://example.com/images/smartphones.jpg",
        contentUrl = "https://theverge.com/new-smartphone-releases",
    ),
    Article(
        id = "3",
        source = "BBC News",
        title = "Global Climate Change",
        description = null,
        content = "Climate change is a critical issue affecting our planet. The latest reports highlight the urgent need for action to mitigate its impacts.",
        imageURL = null,
        contentUrl = "https://bbc.com/global-climate-change",
    ),
    Article(
        id = "4",
        source = "Wired",
        title = "Cybersecurity Trends in 2024",
        description = "Exploring the emerging trends in cybersecurity and what businesses need to know.",
        content = "As cyber threats evolve, so do the strategies to counteract them. This article delves into the top cybersecurity trends and best practices for safeguarding your digital assets.",
        imageURL = "https://example.com/images/cybersecurity-trends.jpg",
        contentUrl = "https://wired.com/cybersecurity-trends-2024",
    ),
    Article(
        id = "5",
        source = "National Geographic",
        title = "Exploring the Depths of the Ocean",
        description = "A deep dive into the mysteries of the ocean and its diverse ecosystems.",
        content = "The ocean remains one of the least explored frontiers on Earth. This article takes you on a journey through its depths, uncovering its secrets and wonders.",
        imageURL = "https://example.com/images/ocean-depths.jpg",
        contentUrl = "https://nationalgeographic.com/exploring-ocean-depths",
    ),
)
