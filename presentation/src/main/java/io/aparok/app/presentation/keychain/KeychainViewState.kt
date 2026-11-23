package io.aparok.app.presentation.keychain

sealed class KeychainViewState {
    data class Displayed(val mnemonic: String) : KeychainViewState()
    object Blurred : KeychainViewState()
}