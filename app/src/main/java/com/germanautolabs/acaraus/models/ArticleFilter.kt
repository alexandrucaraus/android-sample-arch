package com.germanautolabs.acaraus.models

import java.time.LocalDate

data class ArticleFilter(
    val query: String = "",
    val sortedBy: SortBy = SortBy.MostRecent,
    val language: String = "",
    val sources: List<ArticleSource> = emptyList(),
    val fromDate: LocalDate = LocalDate.now(),
    val toDate: LocalDate = LocalDate.now().minusMonths(1),
)

enum class SortBy {
    Relevancy,
    Popularity,
    MostRecent,
}

data class ArticleSource(
    val name: String,
)
