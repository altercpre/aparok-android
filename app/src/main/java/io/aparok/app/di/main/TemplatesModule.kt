package io.aparok.app.di.main

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.presentation.templates.DefaultObjectTypeTemplatesContainer
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TemplatesModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideStorelessContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer =
        StorelessSubscriptionContainer.Impl(repo, channel, dispatchers, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideTemplatesContainer(
        storage: StorelessSubscriptionContainer
    ): ObjectTypeTemplatesContainer =
        DefaultObjectTypeTemplatesContainer(
            storage = storage
        )
}