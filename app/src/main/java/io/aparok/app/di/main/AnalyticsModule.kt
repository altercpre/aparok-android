package io.aparok.app.di.main

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.analytics.base.DefaultAnalytics
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.analytics.DefaultAnalyticsParamsProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AnalyticsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnalytics(): Analytics = DefaultAnalytics()

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnalyticsParams(
        userPermissionProvider: UserPermissionProvider,
        spaceViewContainer: SpaceViewSubscriptionContainer
    ): AnalyticSpaceHelperDelegate = DefaultAnalyticsParamsProvider(
        userPermissionProvider = userPermissionProvider,
        spaceViewContainer = spaceViewContainer
    )
}
