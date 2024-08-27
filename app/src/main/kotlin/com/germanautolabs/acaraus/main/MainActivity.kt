package com.germanautolabs.acaraus.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.germanautolabs.acaraus.screens.articles.list.ArticleListNode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    @SuppressLint("RestrictedApi")
    private fun render() {
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MainNavHost(
                modifier = Modifier.navigationBarsPadding(),
                navController = navController,
                startDestination = ArticleListNode,
            )
        }
    }
}
