package io.aparok.app.domain.base

import io.aparok.app.domain.error.Error

@Deprecated(
    message = "Result is deprecated",
    replaceWith = ReplaceWith("Resultat", "io.aparok.app.domain.base")
)
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val error: Error) : Result<T>()
}