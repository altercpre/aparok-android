package io.aparok.app.domain.spaces

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.misc.OpenObjectNavigation
import io.aparok.app.core_models.misc.navigation
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.search.SearchObjects
import javax.inject.Inject

class ResolveSpaceHomepage @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val searchObjects: SearchObjects
) : ResultInteractor<ResolveSpaceHomepage.Params, ResolveSpaceHomepage.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result {
        val homepage = spaceViews.get(params.space)?.homepage
        if (homepage.isNullOrEmpty() || homepage in HOMEPAGE_SPECIAL_CONSTANTS) {
            return Result.Widgets
        }
        val searchResult = searchObjects.invoke(
            params = SearchObjects.Params(
                space = params.space,
                filters = listOf(
                    DVFilter(
                        relation = Relations.ID,
                        value = homepage,
                        condition = DVFilterCondition.EQUAL
                    )
                ),
                keys = listOf(Relations.ID, Relations.LAYOUT, Relations.SPACE_ID),
                limit = 1
            )
        )
        val obj = searchResult.getOrNull()?.firstOrNull() ?: return Result.Widgets
        return when (val nav = obj.navigation()) {
            is OpenObjectNavigation.OpenParticipant,
            is OpenObjectNavigation.UnexpectedLayoutError,
            OpenObjectNavigation.NonValidObject -> Result.Widgets
            is OpenObjectNavigation.OpenBookmarkUrl -> Result.Object(
                OpenObjectNavigation.OpenEditor(
                    target = homepage,
                    space = obj.spaceId ?: params.space.id
                )
            )
            else -> Result.Object(nav)
        }
    }

    data class Params(val space: SpaceId)

    sealed class Result {
        data object Widgets : Result()
        data class Object(val navigation: OpenObjectNavigation) : Result()
    }

    companion object {
        const val HOMEPAGE_WIDGETS_VALUE = "widgets"
        val HOMEPAGE_SPECIAL_CONSTANTS = setOf(HOMEPAGE_WIDGETS_VALUE, "graph", "lastOpened")
    }
}
