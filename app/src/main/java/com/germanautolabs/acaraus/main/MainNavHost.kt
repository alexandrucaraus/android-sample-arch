package com.germanautolabs.acaraus.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.germanautolabs.acaraus.screens.articles.details.articleDetailScreen
import com.germanautolabs.acaraus.screens.articles.details.onNavigateToDetails
import com.germanautolabs.acaraus.screens.articles.list.ARTICLE_LIST_ROUTE
import com.germanautolabs.acaraus.screens.articles.list.articleListScreen

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues,
    route: String = ARTICLE_LIST_ROUTE,
) = NavHost(
    navController = navController,
    startDestination = route,
    modifier = modifier.padding(paddingValues = innerPadding),
) {
    articleListScreen(
        modifier = modifier,
        onNavigateToDetails = navController::onNavigateToDetails,
    )

    articleDetailScreen(
        modifier = modifier,
        onNavigateBack = navController::popBackStack,
    )
}
