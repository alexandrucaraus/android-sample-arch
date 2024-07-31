package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.germanautolabs.acaraus.main.MainActivity
import org.junit.Rule
import org.junit.Test

class ArticleListScreenTest {

    @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testArticleListScreenToArticleDetailScreenNavigationAndBack() {

        composeTestRule.onNodeWithText("Title: Old news").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Article Detail Screen article: 0").assertIsDisplayed()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Close").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Title: Old news").assertIsDisplayed()

    }
}
