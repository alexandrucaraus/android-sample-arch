package com.germanautolabs.acaraus.main

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

    private fun render() {
        enableEdgeToEdge()
        setContent {
            MainNavHost(
                modifier = Modifier.navigationBarsPadding(),
                navController = rememberNavController(),
                startDestination = ArticleListNode,
            )
        }
    }
}
