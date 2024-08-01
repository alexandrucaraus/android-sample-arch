package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.germanautolabs.acaraus.main.MainActivity
import org.junit.Rule
import org.junit.Test

class ArticleListScreenTest {

    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testArticleListScreenToArticleDetailScreenNavigationAndBack() {
        composeTestRule.onNodeWithText("Old news").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("If you're someone who's got an iPhone", substring = true).assertIsDisplayed()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("Close").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Old news").assertIsDisplayed()
    }
}
