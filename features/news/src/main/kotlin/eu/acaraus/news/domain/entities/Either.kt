package eu.acaraus.news.domain.entities

import eu.acaraus.news.domain.entities.Either.Companion.success
import eu.acaraus.news.domain.entities.Either.Error
import eu.acaraus.news.domain.entities.Either.Success

sealed class Either<out S, out E> {

    val isSuccess get() = this is Success
    val isError get() = this is Error

    data class Success<S>(val value: S) : Either<S, Nothing>()
    data class Error<E>(val error: E) : Either<Nothing, E>()

    companion object {
        fun <S, E> success(s: S): Either<S, E> = Success(s)
        fun <S, E> error(e: E): Either<S, E> = Error(e)
    }
}

fun <S, E> Either<S, E>.map(transform: (S) -> S): Either<S, E> = when (this) {
    is Success -> success<S, Nothing>(transform(value))
    is Error -> this
}

fun <S, E> Either<S, E>.onEachSuccess(block: (S) -> Unit): Either<S, E> = when (this) {
    is Success -> {
        block(value)
        this
    }

    is Error -> this
}

fun <S, E> Either<S, E>.onEachError(block: (E) -> Unit): Either<S, E> = when (this) {
    is Success -> this
    is Error -> {
        block(error)
        this
    }
}

fun <S, E> Either<S, E>.fold(
    onSuccess: (S) -> Unit,
    onError: (E) -> Unit = { },
): Either<S, E> {
    when (this) {
        is Success -> onSuccess(value)
        is Error -> onError(error)
    }
    return this
}
