package com.germanautolabs.acaraus.screens.articles.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.germanautolabs.acaraus.models.Article
import com.germanautolabs.acaraus.screens.articles.list.components.ArticleListState
import com.germanautolabs.acaraus.screens.articles.list.components.ArticlesFilterEditorState
import com.germanautolabs.acaraus.screens.articles.list.components.AudioCommandButtonState
import com.germanautolabs.acaraus.screens.components.ToasterState

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ArticleListStateSuccessPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(
            list = articles,
        ),
        articlesFilterEditorState = ArticlesFilterEditorState(
            query = "something",
        ),
        toasterState = ToasterState(),
        audioCommandButtonState = AudioCommandButtonState(
            hasSpeechRecognition = true,
        ),
        onNavigateToDetails = {},
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ArticleListStateLoadingPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(isLoading = true),
        articlesFilterEditorState = ArticlesFilterEditorState(),
        toasterState = ToasterState(),
        audioCommandButtonState = AudioCommandButtonState(),
        onNavigateToDetails = {},
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ArticleListStateErrorPreview() {
    ArticleListScreen(
        modifier = Modifier.fillMaxSize(),
        articleListState = ArticleListState(isError = true, errorMessage = "null"),
        articlesFilterEditorState = ArticlesFilterEditorState(),
        toasterState = ToasterState(),
        audioCommandButtonState = AudioCommandButtonState(),
        onNavigateToDetails = {},
    )
}

private val articles = listOf(
    Article(
        id = "1",
        imageURL = "https://picsum.photos/200",
        title = "Samsung's Galaxy Z Fold 5 Is Cheaper Than Ever",
        source = "The Verge",
        contentUrl = "https://www.theverge.com/deals/2023/12/28/24022654/samsung-galaxy-z-fold-5-sale-amazon-best-buy",
        description = "Foldable phone gets a price cut.",
        content = "Samsung's latest foldable phone, the Galaxy Z Fold 5, is now available at its lowest price ever. Both Amazon and Best Buy have the 256GB model for \$1,399.99, a \$400 discount from its original price. This is a great opportunity to snag a high-end foldable phone with a stunning display and powerful performance.",
    ),
    Article(
        id = "2",
        imageURL = "https://picsum.photos/200",
        title = "Google Pixel 8 Pro Review: The Best Android Phone Yet?",
        source = "Android Central",
        contentUrl = "https://www.androidcentral.com/phones/google-pixel-8-pro-review",
        description = "Google's latest flagship phone reviewed.",
        content = "The Google Pixel 8 Pro is here, and it's packed with new features like a temperature sensor, improved cameras, and the latest Tensor G3 chip. Is it enough to make it the best Android phone on the market? Find out in our comprehensive review.",
    ),
    Article(
        id = "3",
        imageURL = "https://picsum.photos/200",
        title = "Top 5 Best Wireless Earbuds for 2024",
        source = "TechRadar",
        contentUrl = "https://www.techradar.com/news/audio/best-wireless-earbuds",
        description = "Find the perfect earbuds for your needs.",
        content = "Looking for a new pair of wireless earbuds? We've tested and reviewed the latest models from Apple, Sony, Bose, and more to bring you our top picks for the best wireless earbuds of 2024.",
    ),
    Article(
        id = "4",
        imageURL = "https://picsum.photos/200",
        title = "The Future of AI: What to Expect in the Next Decade",
        source = "Wired",
        contentUrl = "https://www.wired.com/story/the-future-of-ai/",
        description = "Exploring the potential of artificial intelligence.",
        content = "Artificial intelligence is rapidly evolving, with new breakthroughs happening all the time. From self-driving cars to personalized medicine, AI has the potential to revolutionize many aspects of our lives. This article explores the latest advancements in AI and what we can expect in the years to come.",
    ),
)
