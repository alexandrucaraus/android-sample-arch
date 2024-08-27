package com.germanautolabs.acaraus.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.germanautolabs.acaraus.screens.articles.details.articleDetailNavNode
import com.germanautolabs.acaraus.screens.articles.details.onNavigateToArticleDetail
import com.germanautolabs.acaraus.screens.articles.list.ArticleListNode
import com.germanautolabs.acaraus.screens.articles.list.articlesListNavNode

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any = ArticleListNode,
) = NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
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
