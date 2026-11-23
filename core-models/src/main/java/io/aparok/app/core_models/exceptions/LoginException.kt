package io.aparok.app.core_models.exceptions

sealed class LoginException : Exception() {
    class InvalidMnemonic : LoginException()
    class NetworkIdMismatch: LoginException()
    class FailedToFindAccountInfo: LoginException()
}