package io.aparok.app.feature_allcontent.models

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.library.StoreSearchParams
import io.aparok.app.presentation.objects.ObjectsListSort
import io.aparok.app.presentation.objects.toDVSort
import io.aparok.app.presentation.search.ObjectSearchConstants.defaultKeys
import io.aparok.app.presentation.search.buildChatsFilter
import io.aparok.app.presentation.search.buildDeletedFilter
import io.aparok.app.presentation.search.buildLayoutFilter
import io.aparok.app.presentation.search.buildLimitedObjectIdsFilter
import io.aparok.app.presentation.search.buildSpaceIdFilter
import io.aparok.app.presentation.search.buildTemplateFilter
import io.aparok.app.presentation.search.buildUnlinkedObjectFilter

val allContentTabLayouts = mapOf(
    AllContentTab.PAGES to listOf(
        ObjectType.Layout.BASIC,
        ObjectType.Layout.PROFILE,
        ObjectType.Layout.TODO,
        ObjectType.Layout.NOTE
    ),
    AllContentTab.LISTS to listOf(
        ObjectType.Layout.SET,
        ObjectType.Layout.COLLECTION,
        ObjectType.Layout.OBJECT_TYPE,
    ),
    AllContentTab.FILES to listOf(
        ObjectType.Layout.FILE,
        ObjectType.Layout.PDF
    ),
    AllContentTab.MEDIA to listOf(
        ObjectType.Layout.IMAGE,
        ObjectType.Layout.VIDEO,
        ObjectType.Layout.AUDIO
    ),
    AllContentTab.BOOKMARKS to listOf(
        ObjectType.Layout.BOOKMARK
    )
)

// Function to create subscription params
fun createSubscriptionParams(
    spaceId: Id,
    activeMode: UiTitleState,
    activeTab: AllContentTab,
    activeSort: ObjectsListSort,
    limitedObjectIds: List<String>,
    limit: Int,
    subscriptionId: String
): StoreSearchParams {
    val (filters, sorts) = activeTab.filtersForSubscribe(
        spaces = listOf(spaceId),
        activeSort = activeSort,
        limitedObjectIds = limitedObjectIds,
        activeMode = activeMode
    )
    return StoreSearchParams(
        space = SpaceId(spaceId),
        filters = filters,
        sorts = sorts,
        keys = defaultKeys,
        limit = limit,
        subscription = subscriptionId
    )
}

fun AllContentTab.filtersForSubscribe(
    spaces: List<Id>,
    activeSort: ObjectsListSort,
    limitedObjectIds: List<Id>,
    activeMode: UiTitleState
): Pair<List<DVFilter>, List<DVSort>> {
    val tab = this
    when (this) {
        AllContentTab.PAGES,
        AllContentTab.LISTS,
        AllContentTab.FILES,
        AllContentTab.MEDIA,
        AllContentTab.BOOKMARKS -> {
            val filters = buildList {
                addAll(buildDeletedFilter())
                add(buildLayoutFilter(layouts = allContentTabLayouts.getValue(tab)))
                add(buildSpaceIdFilter(spaces))
                add(buildTemplateFilter())
                add(buildChatsFilter())
                if (limitedObjectIds.isNotEmpty()) {
                    add(buildLimitedObjectIdsFilter(limitedObjectIds = limitedObjectIds))
                }
                if (activeMode == UiTitleState.OnlyUnlinked) {
                    addAll(buildUnlinkedObjectFilter())
                }
            }
            val sorts = listOf(activeSort.toDVSort())
            return filters to sorts
        }
    }
}

fun AllContentTab.filtersForSearch(
    spaces: List<Id>
): List<DVFilter> {
    val tab = this
    val filters = buildList {
        addAll(buildDeletedFilter())
        add(buildSpaceIdFilter(spaces))
        add(buildTemplateFilter())
    }
    return filters
}