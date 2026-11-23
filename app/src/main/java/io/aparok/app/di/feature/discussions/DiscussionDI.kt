package io.aparok.app.di.feature.discussions

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.feature.chats.ChatContainerModule
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.feature_discussions.presentation.DiscussionViewModel
import io.aparok.app.feature_discussions.presentation.DiscussionViewModelFactory
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.defaultCopyFileToCacheDirectory
import io.aparok.app.ui.discussions.DiscussionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [DiscussionComponentDependencies::class],
    modules = [
        ChatContainerModule::class,
        DiscussionModule.Declarations::class,
        DiscussionModule.Providers::class
    ]
)
@PerScreen
interface DiscussionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: DiscussionViewModel.Params): Builder
        fun withDependencies(dependencies: DiscussionComponentDependencies): Builder
        fun build(): DiscussionComponent
    }
    fun inject(fragment: DiscussionFragment)
}

@Module
object DiscussionModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DiscussionViewModelFactory
        ): ViewModelProvider.Factory
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @PerScreen
        fun provideCopyFileToCache(
            context: Context
        ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)
    }
}

interface DiscussionComponentDependencies : ComponentDependencies {
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun blockRepository(): BlockRepository
    fun authRepo(): AuthRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun chatEventChannel(): ChatEventChannel
    fun logger(): Logger
    fun members(): ActiveSpaceMemberSubscriptionContainer
    fun storelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun dateProvider(): DateProvider
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun fieldParser(): FieldParser
    fun context(): Context
}
