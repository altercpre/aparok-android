package io.aparok.app.di.feature.onboarding

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.CrashReporter
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.CreateAccount
import io.aparok.app.domain.auth.interactor.SetupWallet
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.ImportGetStartedUseCase
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.RelationsSubscriptionManager
import io.aparok.app.domain.spaces.SpaceDeletedStatusWatcher
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.onboarding.OnboardingStartViewModel
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    dependencies = [OnboardingStartDependencies::class],
    modules = [
        OnboardingStartModule::class,
        OnboardingStartModule.Declarations::class
    ]
)
@AuthScreenScope
interface OnboardingStartComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingStartDependencies): OnboardingStartComponent
    }

    fun getViewModel(): OnboardingStartViewModel
}

@Module
object OnboardingStartModule {

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideCreateAccountUseCase(
        authRepository: AuthRepository,
        configStorage: ConfigStorage,
        initialParamsProvider: InitialParamsProvider,
        awaitAccountStartManager: AwaitAccountStartManager,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): CreateAccount = CreateAccount(
        repository = authRepository,
        configStorage = configStorage,
        initialParamsProvider = initialParamsProvider,
        dispatcher = dispatchers,
        awaitAccountStartManager = awaitAccountStartManager,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideSetupWalletUseCase(
        authRepository: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetupWallet = SetupWallet(
        repository = authRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideSetupSkipUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) = ImportGetStartedUseCase(
        repo = repository,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingStartViewModel.Factory): ViewModelProvider.Factory
    }
}

interface OnboardingStartDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun blockRepository(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun authRepository(): AuthRepository
    fun userSettings(): UserSettingsRepository
    fun relationsSubscriptionManager(): RelationsSubscriptionManager
    fun pathProvider(): PathProvider
    fun metricsProvider(): InitialParamsProvider
    fun crashReporter(): CrashReporter
    fun spaceManager(): SpaceManager
    fun spaceStatusWatcher(): SpaceDeletedStatusWatcher
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun stringResourceProvider(): StringResourceProvider
    fun providePendingIntentStore(): PendingIntentStore
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthScreenScope