package com.germanautolabs.acaraus.models

import java.time.LocalDate

data class ArticlesFilter(
    val query: String = "",
    val sortedBy: SortBy = SortBy.MostRecent,
    val language: String = "",
    val sources: List<ArticlesSources> = emptyList(),
    val fromDate: LocalDate = LocalDate.now(),
    val toDate: LocalDate = LocalDate.now().minusMonths(1),
)

enum class SortBy {
    Relevancy,
    Popularity,
    MostRecent,
}

data class ArticlesSources(
    val id: String,
    val name: String,
    val language: String,
    val category: String,
)
