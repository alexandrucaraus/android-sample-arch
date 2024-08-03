package com.germanautolabs.acaraus.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.germanautolabs.acaraus.screens.articles.details.articleDetailNavNode
import com.germanautolabs.acaraus.screens.articles.details.onNavigateToArticleDetail
import com.germanautolabs.acaraus.screens.articles.list.ArticleListNode
import com.germanautolabs.acaraus.screens.articles.list.articleListNavNode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

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
    articleListNavNode(
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

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(URLDecoder.decode(value, "UTF-8"))

    override fun serializeAsValue(value: T): String = URLEncoder.encode(json.encodeToString(value), "UTF-8")

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}
