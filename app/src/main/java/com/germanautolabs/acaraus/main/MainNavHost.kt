package com.germanautolabs.acaraus.main

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.germanautolabs.acaraus.screens.articles.details.articleDetailNavNode
import com.germanautolabs.acaraus.screens.articles.details.onNavigateToArticleDetail
import com.germanautolabs.acaraus.screens.articles.list.ArticleListNode
import com.germanautolabs.acaraus.screens.articles.list.articlesListNavNode

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues,
) = NavHost(
    navController = navController,
    startDestination = ArticleListNode,
    modifier = modifier.padding(paddingValues = innerPadding),
) {
    articlesListNavNode(
        modifier = modifier,
        onNavigateToDetails = navController::onNavigateToArticleDetail,
    )

    articleDetailNavNode(
        modifier = modifier,
        onNavigateBack = navController::popBackStack,
    )
}

fun launchBrowserCustomTab(activityContext: Context, url: String) {
    val uri = Uri.parse(url)
    val customTabsIntent = CustomTabsIntent.Builder().setShowTitle(true).build()
    customTabsIntent.launchUrl(activityContext, uri)
}
