package eu.acaraus.core

import eu.acaraus.core.Either.Companion.success

sealed class Either<out E,out S> {

    val isSuccess get() = this is Success
    val isError get() = this is Error

    data class Success<S>(val value: S) : Either<Nothing,S>()
    data class Error<E>(val error: E) : Either<E, Nothing>()

    companion object {
        fun <E, S> success(s: S): Either<E, S> = Success(s)
        fun <E, S> error(e: E): Either<E, S> = Error(e)
    }
}

fun <E, S> Either<E, S>.map(transform: (S) -> S): Either<E, S> = when (this) {
    is Either.Success -> success<Nothing, S>(transform(value))
    is Either.Error -> this
}

fun <E, S> Either<E, S>.onEachSuccess(block: (S) -> Unit): Either<E, S> = when (this) {
    is Either.Success -> {
        block(value)
        this
    }
    is Either.Error -> this
}

fun <E, S> Either<E, S>.onEachError(block: (E) -> Unit): Either<E, S> = when (this) {
    is Either.Success -> this
    is Either.Error -> {
        block(error)
        this
    }
}

fun <E, S> Either<E, S>.fold(
    onSuccess: (S) -> Unit,
    onError: (E) -> Unit = { },
): Either<E, S> {
    when (this) {
        is Either.Success -> onSuccess(value)
        is Either.Error -> onError(error)
    }
    return this
}
