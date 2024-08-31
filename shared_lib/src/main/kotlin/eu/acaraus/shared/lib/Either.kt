package eu.acaraus.shared.lib

import eu.acaraus.shared.lib.Either.Companion.success


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
    is Either.Success -> success<S, Nothing>(transform(value))
    is Either.Error -> this
}

fun <S, E> Either<S, E>.onEachSuccess(block: (S) -> Unit): Either<S, E> = when (this) {
    is Either.Success -> {
        block(value)
        this
    }
    is Either.Error -> this
}

fun <S, E> Either<S, E>.onEachError(block: (E) -> Unit): Either<S, E> = when (this) {
    is Either.Success -> this
    is Either.Error -> {
        block(error)
        this
    }
}

fun <S, E> Either<S, E>.fold(
    onSuccess: (S) -> Unit,
    onError: (E) -> Unit = { },
): Either<S, E> {
    when (this) {
        is Either.Success -> onSuccess(value)
        is Either.Error -> onError(error)
    }
    return this
}
