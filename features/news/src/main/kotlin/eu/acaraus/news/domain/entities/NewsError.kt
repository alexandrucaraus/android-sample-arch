package eu.acaraus.news.domain.entities

data class NewsError(
    val code: String,
    val message: String,
)

fun Throwable.toNewsError(
    code: String = "unknown",
    message: String = this.message ?: "unknown",
): NewsError = NewsError(code = code, message = message)
