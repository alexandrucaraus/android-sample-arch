package eu.acaraus.sample.arch.test.news.presentation.articles.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.acaraus.news.domain.entities.Article
import eu.acaraus.sample.arch.Activity
import org.junit.Rule
import org.junit.Test

class ArticleListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<Activity>()

    @Test
    fun testArticleListScreenToArticleDetailScreenNavigationAndBack() {
        composeTestRule.waitUntil(TIMEOUT) {
            composeTestRule.onNodeWithTag("ListItem0").isDisplayed()
        }

        composeTestRule.onNodeWithTag("ListItem0").performClick()

        composeTestRule.onNodeWithTag("Close").performClick()

        composeTestRule.waitUntil(TIMEOUT) {
            composeTestRule.onNodeWithTag("ListItem0").isDisplayed()
        }

        composeTestRule.onNodeWithTag("ListItem0").assertIsDisplayed()
    }

    @Test
    fun testArticleListOpenFilter() {
        composeTestRule.waitUntil(TIMEOUT) {
            composeTestRule.onNodeWithTag("ArticleFilter").isDisplayed()
        }

        composeTestRule.onNodeWithTag("ArticleFilter").performClick()

        composeTestRule.onNodeWithText("Reset").performClick()
    }

    companion object {
        const val TIMEOUT = 5000L
    }
}

// TODO test the navigation with the following article

val bugArticle = Article(
    id = "35196327",
    source = "Forbes",
    title = "Bitcoin Prices Plunge Below $53,000 As Multiple Factors Fuel Losses - Forbes",
    description = "Bitcoin prices took a tumble today, falling close to 8% in less than 24 hours as markets responded to several bearish variables including lackluster jobs data.",
    content = "Bitcoin prices approached $52,000 earlier today. (Photo by Chesnot/Getty Images) Getty Images Bitcoin prices took a tumble today, falling close to 8% in less than 24 hours as markets responded to s… [+4975 chars]",
    imageURL = "https://imageio.forbes.com/specials-images/imageserve/636c073967f1b835c87fde53/0x0.jpg?format=jpg&height=900&width=1600&fit=bounds",
    contentUrl = "https://www.forbes.com/sites/digital-assets/2024/09/06/bitcoin-prices-plunge-below-53000-as-multiple-factors-fuel-losses/",
)
