package io.aparok.app.domain.auth

import io.aparok.app.domain.auth.interactor.SetupWallet
import io.aparok.app.domain.auth.model.Wallet
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.core_models.CoroutineTestRule
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.common.DefaultCoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class SetupWalletTest {

    @get:Rule
    var rule = DefaultCoroutineTestRule()

    @Mock
    lateinit var repo: AuthRepository

    lateinit var dispatchers: AppCoroutineDispatchers

    lateinit var setupWallet: SetupWallet

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        setupWallet = SetupWallet(repo, dispatchers)
    }

    @Test
    fun `should successfully create wallet and successfully save mnemonic`() = runBlocking {

        val path = "path"
        val mnemonic = "mnemonic"

        val wallet = Wallet(
            mnemonic = mnemonic
        )

        val param = SetupWallet.Params(path)

        repo.stub {
            onBlocking { createWallet(path) } doReturn wallet
        }

        repo.stub {
            onBlocking { saveMnemonic(mnemonic) } doReturn Unit
        }

        setupWallet.run(param)

        verify(repo, times(1)).createWallet(path)
        verify(repo, times(1)).saveMnemonic(mnemonic)
        verifyNoMoreInteractions(repo)
    }
}