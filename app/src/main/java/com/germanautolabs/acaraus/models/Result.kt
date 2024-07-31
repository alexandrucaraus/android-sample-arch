package com.germanautolabs.acaraus.models

class Result<S, E> private constructor(val success: S? = null, val error: E? = null) {

    val isSuccess = success != null
    val isError = error != null

    companion object {
        fun <S, E> success(s: S) = Result<S, E>(success = s, error = null)
        fun <S, E> error(e: E) = Result<S, E>(success = null, error = e)
    }
}
