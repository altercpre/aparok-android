package io.aparok.app.di.main

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.aparok.app.BuildConfig
import io.aparok.app.app.DefaultFeatureToggles
import io.aparok.app.app.TogglePrefs
import io.aparok.app.core_utils.tools.AppInfo
import io.aparok.app.core_utils.tools.DefaultAppInfo
import io.aparok.app.core_utils.tools.DefaultThreadInfo
import io.aparok.app.core_utils.tools.DefaultUrlValidator
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.core_utils.tools.ThreadInfo
import io.aparok.app.core_utils.tools.UrlValidator
import io.aparok.app.device.providers.AppDefaultDateFormatProvider
import io.aparok.app.device.providers.DateProviderImpl
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.debugging.DebugConfig
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.DateTypeNameProvider
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.objects.GetDateObjectByTimestamp
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.FieldParserImpl
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.middleware.interactor.MiddlewareProtobufLogger
import io.aparok.app.middleware.interactor.ProtobufConverterProvider
import io.aparok.app.other.BasicLogger
import io.aparok.app.other.DefaultDateTypeNameProvider
import io.aparok.app.other.DefaultDebugConfig
import io.aparok.app.presentation.util.StringResourceProviderImpl
import io.aparok.app.presentation.util.UrlHelper
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.presentation.widgets.collection.ResourceProviderImpl
import io.aparok.app.util.UrlHelperImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.time.ZoneId
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module(includes = [UtilModule.Bindings::class])
object UtilModule {

    @JvmStatic
    @Provides
    @Singleton
    @TogglePrefs
    fun providesSharedPreferences(
        context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppInfo(): AppInfo = DefaultAppInfo(BuildConfig.VERSION_NAME)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDateProvider(
        localeProvider: LocaleProvider,
        appDefaultDateFormatProvider: AppDefaultDateFormatProvider,
        stringResourceProvider: StringResourceProvider
    ): DateProvider = DateProviderImpl(
        defaultZoneId = ZoneId.systemDefault(),
        localeProvider = localeProvider,
        appDefaultDateFormatProvider = appDefaultDateFormatProvider,
        stringResourceProvider = stringResourceProvider
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideDateByTimestamp(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDateObjectByTimestamp = GetDateObjectByTimestamp(repository, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFieldsProvider(
        dateProvider: DateProvider,
        logger: Logger,
        getDateObjectByTimestamp: GetDateObjectByTimestamp,
        stringResourceProvider: StringResourceProvider
    ): FieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)

    @JvmStatic
    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider =
        ResourceProviderImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideStringResourceProvider(context: Context): StringResourceProvider =
        StringResourceProviderImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @JvmStatic
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUrlHelper(): UrlHelper = UrlHelperImpl()

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun bindLogger(logger: BasicLogger): Logger

        @Binds
        @Singleton
        fun bindDebugConfig(config: DefaultDebugConfig): DebugConfig

        @Binds
        @Singleton
        fun bindThreadInfo(info: DefaultThreadInfo): ThreadInfo

        @Binds
        @Singleton
        fun bindUrlValidator(applicator: DefaultUrlValidator): UrlValidator

        @Binds
        @Singleton
        fun bindFeatureToggles(applicator: DefaultFeatureToggles): FeatureToggles

        @Binds
        @Singleton
        fun bindMiddlewareProtobufLogger(logger: MiddlewareProtobufLogger.Impl): MiddlewareProtobufLogger

        @Binds
        @Singleton
        fun bindProtobufConverterProvider(provider: ProtobufConverterProvider.Impl): ProtobufConverterProvider

        @Binds
        @Singleton
        fun bindDateTypeNameProvider(provider: DefaultDateTypeNameProvider): DateTypeNameProvider
    }
}