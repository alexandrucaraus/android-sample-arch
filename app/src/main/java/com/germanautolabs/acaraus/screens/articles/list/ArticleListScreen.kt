package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilter
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleFilterState
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleList
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    articleListState: ArticleListState,
    articleFilterState: ArticleFilterState,
    onNavigateToDetails: (article: Article) -> Unit,
) = Scaffold(modifier = modifier, topBar = {
    TopAppBar(
        title = { Text("News") },
        actions = {
            IconButton(onClick = articleFilterState.show) {
                Box {
                    if (articleFilterState.isSet) {
                        Box(
                            modifier = Modifier.align(Alignment.BottomCenter)
                                .background(Color.Red, shape = CircleShape)
                                .size(8.dp),
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        null,
                    )
                }
            }
        },
    )
}, floatingActionButton = {
    FloatingActionButton(onClick = articleListState.toggleListening) {
        Icon(Icons.Default.Mic, contentDescription = "Listen commands")
    }
}, content = { padding ->
    Column {
        ArticleList(
            modifier = Modifier.padding(padding),
            listState = articleListState,
            onNavigateToDetails = onNavigateToDetails,
        )

        ArticleFilter(filterState = articleFilterState)
    }
})
