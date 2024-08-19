package com.germanautolabs.acaraus.models

data class Error(
    val code: String,
    val message: String,
)

fun Throwable.toError(
    code: String = "unknown",
    message: String = this.message ?: "unknown",
): Error = Error(code = code, message = message)
