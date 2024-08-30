package eu.acaraus.news.screens.articles.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.acaraus.news.Activity
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
