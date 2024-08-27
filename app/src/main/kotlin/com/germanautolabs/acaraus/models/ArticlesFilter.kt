package com.germanautolabs.acaraus.models

import java.time.LocalDate

data class ArticlesFilter(
    val query: String = "",
    val sortedBy: SortBy = SortBy.MostRecent,
    val language: String = "en",
    val sources: List<ArticlesSources> = emptyList(),
    val fromDate: LocalDate = LocalDate.now().minusMonths(1),
    val toDate: LocalDate = LocalDate.now(),
)

enum class SortBy {
    Relevancy,
    Popularity,
    MostRecent, ;

    companion object {
        fun toSet(): Set<String> = SortBy.entries.map { it.name }.toSet()
    }
}

data class ArticlesSources(
    val id: String,
    val name: String,
    val language: String,
    val category: String,
)
