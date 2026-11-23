package io.aparok.app.presentation.search

import io.aparok.app.presentation.navigation.DefaultSearchItem

sealed class ObjectSearchSection : DefaultSearchItem {
    data object RecentlyOpened: ObjectSearchSection()
    sealed class SelectWidgetSource : ObjectSearchSection() {
        data object FromMyObjects: SelectWidgetSource()
        data object FromLibrary: SelectWidgetSource()
        data object System: SelectWidgetSource()
        data object Suggested: SelectWidgetSource()
    }
}