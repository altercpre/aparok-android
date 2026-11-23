package io.aparok.app.di.main

import android.content.Context
import io.aparok.app.domain.widgets.OsWidgetDataViewSync
import io.aparok.app.feature_os_widgets.ui.OsDataViewWidgetUpdater

/**
 * Decorator for [OsWidgetDataViewSync] that also triggers a widget refresh
 * after syncing items to DataStore.
 */
class OsWidgetDataViewSyncWithUpdate(
    private val delegate: OsWidgetDataViewSync,
    private val context: Context
) : OsWidgetDataViewSync {

    override suspend fun sync() {
        delegate.sync()
        OsDataViewWidgetUpdater.updateAllWidgets(context)
    }
}
