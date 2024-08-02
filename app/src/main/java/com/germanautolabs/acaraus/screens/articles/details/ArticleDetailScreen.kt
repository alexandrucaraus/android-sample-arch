package com.germanautolabs.acaraus.screens.articles.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.germanautolabs.acaraus.main.launchBrowserCustomTab

data class ArticleDetailState(
    val imageUrl: String,
    val title: String,
    val content: String,
    val contentUrl: String,
    val source: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    state: ArticleDetailState,
    onNavigateBack: () -> Unit,
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = {
                Text(
                    text = state.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            navigationIcon = {
                IconButton(
                    modifier = Modifier.testTag("Close"),
                    onClick = { onNavigateBack() },
                ) {
                    Icon(imageVector = Icons.Default.Close, null)
                }
            },
        )
    },
) { paddingValues ->
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            model = state.imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            text = state.title,
            style = MaterialTheme.typography.titleLarge,
        )

        val context = LocalContext.current
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .clickable {
                    launchBrowserCustomTab(context, state.contentUrl)
                },
            text = "See original article on ${state.source}",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
            ),
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = state.content,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(
        state = dummyArticleDetails,
        onNavigateBack = {},
    )
}
