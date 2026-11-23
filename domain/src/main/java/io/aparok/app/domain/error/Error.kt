package io.aparok.app.domain.error

sealed class Error {
    object BackwardCompatibility : Error()
    object NotFoundObject : Error()
}