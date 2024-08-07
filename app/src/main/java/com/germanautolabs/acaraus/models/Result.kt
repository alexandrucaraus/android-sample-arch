package com.germanautolabs.acaraus.models

import com.germanautolabs.acaraus.models.Result.Companion.success
import com.germanautolabs.acaraus.models.Result.Error
import com.germanautolabs.acaraus.models.Result.Success

sealed class Result<out S, out E> {

    val isSuccess get() = this is Success
    val isError get() = this is Error

    data class Success<S>(val value: S) : Result<S, Nothing>()
    data class Error<E>(val error: E) : Result<Nothing, E>()

    companion object {
        fun <S, E> success(s: S): Result<S, E> = Success(s)
        fun <S, E> error(e: E): Result<S, E> = Error(e)
    }
}

fun <S, E> Result<S, E>.map(transform: (S) -> S): Result<S, E> = when (this) {
    is Success -> success<S, Nothing>(transform(value))
    is Error -> this
}

fun <S, E> Result<S, E>.onEachSuccess(block: (S) -> Unit): Result<S, E> = when (this) {
    is Success -> {
        block(value)
        this
    }
    is Error -> this
}

fun <S, E> Result<S, E>.onEachError(block: (E) -> Unit): Result<S, E> = when (this) {
    is Success -> this
    is Error -> {
        block(error)
        this
    }
}

fun <S, E> Result<S, E>.fold(
    onSuccess: (S) -> Unit,
    onError: (E) -> Unit = { },
): Result<S, E> {
    when (this) {
        is Success -> onSuccess(value)
        is Error -> onError(error)
    }
    return this
}
