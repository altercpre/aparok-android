package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.domain.auth.interactor.GetMnemonic
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.presentation.keychain.KeychainPhraseViewModelFactory
import io.aparok.app.ui.dashboard.DashboardMnemonicReminderDialog
import io.aparok.app.ui.profile.KeychainPhraseDialog
import dagger.Module
import dagger.Provides
import dagger.Subcomponent


@Subcomponent(
    modules = [KeychainPhraseModule::class]
)
@PerScreen
interface KeychainPhraseSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun keychainPhraseModule(module: KeychainPhraseModule): Builder
        fun build(): KeychainPhraseSubComponent
    }

    fun inject(fragment: KeychainPhraseDialog)
    fun inject(fragment: DashboardMnemonicReminderDialog)
}

@Module
object KeychainPhraseModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideKeychainPhraseViewModelFactory(
        getMnemonic: GetMnemonic,
        analytics: Analytics
    ) = KeychainPhraseViewModelFactory(
        getMnemonic = getMnemonic,
        analytics = analytics
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetMnemonicUseCase(
        repository: AuthRepository
    ): GetMnemonic = GetMnemonic(
        repository = repository
    )
}