package io.aparok.app.di.feature.notifications

import android.content.Context
import io.aparok.app.device.DefaultPushMessageProcessor
import io.aparok.app.device.PushMessageProcessor
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.device.DeviceTokenStoringService
import io.aparok.app.domain.notifications.NotificationBuilder
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.presentation.notifications.CryptoService
import io.aparok.app.presentation.notifications.CryptoServiceImpl
import io.aparok.app.presentation.notifications.DecryptionPushContentService
import io.aparok.app.presentation.notifications.DecryptionPushContentServiceImpl
import io.aparok.app.presentation.notifications.SignatureVerificationService
import io.aparok.app.presentation.notifications.SignatureVerificationServiceImpl
import io.aparok.app.domain.notifications.PushKeyProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

@Singleton
@Component(
    dependencies = [PushContentDependencies::class],
    modules = [PushContentModule::class],
)
interface PushContentComponent {
    @Component.Factory
    interface Factory {
        fun create(dependency: PushContentDependencies): PushContentComponent
    }
}

@Module
object PushContentModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providePushMessageProcessor(
        decryptionService: DecryptionPushContentService,
        notificationBuilder: NotificationBuilder
    ): PushMessageProcessor = DefaultPushMessageProcessor(
        decryptionService = decryptionService,
        notificationBuilder = notificationBuilder
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService = CryptoServiceImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSignatureVerificationService(): SignatureVerificationService = SignatureVerificationServiceImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideDecryptionPushContentService(
        pushKeyProvider: PushKeyProvider,
        cryptoService: CryptoService,
        signatureVerificationService: SignatureVerificationService,
        json: Json
    ): DecryptionPushContentService = DecryptionPushContentServiceImpl(
        pushKeyProvider = pushKeyProvider,
        cryptoService = cryptoService,
        signatureVerificationService = signatureVerificationService,
        json = json
    )
}

interface PushContentDependencies : ComponentDependencies {
    fun deviceTokenSavingService(): DeviceTokenStoringService
    fun pushKeyProvider(): PushKeyProvider
    fun context(): Context
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun dispatchers(): AppCoroutineDispatchers
    fun provider(): StringResourceProvider
    fun notificationBuilder(): NotificationBuilder
    fun json(): Json
    fun authRepository(): AuthRepository
}