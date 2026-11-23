package io.aparok.app.presentation.keychain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.analytics.base.EventsDictionary
import io.aparok.app.analytics.base.EventsDictionary.keychainCopy
import io.aparok.app.analytics.base.EventsDictionary.keychainPhraseScreenShow
import io.aparok.app.analytics.base.EventsPropertiesKey
import io.aparok.app.analytics.base.sendEvent
import io.aparok.app.analytics.props.Props
import io.aparok.app.core_utils.ui.ViewStateViewModel
import io.aparok.app.domain.auth.interactor.GetMnemonic
import timber.log.Timber

class KeychainPhraseViewModel(
    private val getMnemonic: GetMnemonic,
    private val analytics: Analytics
) : ViewStateViewModel<KeychainViewState>() {

    private var mnemonic: String = ""

    init {
        proceedWithGettingMnemonic()
        stateData.postValue(KeychainViewState.Blurred)
    }

    fun sendShowEvent(type: String) {
        viewModelScope.sendEvent(
            analytics = analytics,
            eventName = keychainPhraseScreenShow,
            props = Props(
                mapOf(EventsPropertiesKey.type to type)
            )
        )
    }

    private fun copyClickedFromScreenSettings() {
        viewModelScope.sendEvent(
            analytics = analytics,
            eventName = keychainCopy,
            props = Props(
                mapOf(EventsPropertiesKey.type to EventsDictionary.Type.screenSettings)
            )
        )
    }

    fun onKeychainClicked() {
        stateData.postValue(KeychainViewState.Displayed(mnemonic))
        copyClickedFromScreenSettings()
    }

    fun onRootClicked() {
        stateData.postValue(KeychainViewState.Blurred)
    }

    private fun proceedWithGettingMnemonic() {
        getMnemonic.invoke(viewModelScope, Unit) { result ->
            result.either(
                fnL = { e -> Timber.e(e, "Error while getting mnemonic") },
                fnR = { mnemonic ->
                    this.mnemonic = mnemonic
                    Any()
                }
            )
        }
    }
}

class KeychainPhraseViewModelFactory(
    private val getMnemonic: GetMnemonic,
    private val analytics: Analytics
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KeychainPhraseViewModel(
            getMnemonic = getMnemonic,
            analytics = analytics
        ) as T
    }
}