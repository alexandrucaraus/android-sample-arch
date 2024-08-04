package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.germanautolabs.acaraus.main.MainActivity
import org.junit.Rule
import org.junit.Test

class ArticleListScreenTest {

    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

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

    companion object {
        const val TIMEOUT = 5000L
    }
}
