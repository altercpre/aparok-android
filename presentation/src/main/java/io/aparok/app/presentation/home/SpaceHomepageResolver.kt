package io.aparok.app.presentation.home

import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.domain.spaces.ResolveSpaceHomepage

object SpaceHomepageResolver {

    fun isExplicitObjectHomepage(homepage: String?): Boolean =
        !homepage.isNullOrEmpty() && homepage !in ResolveSpaceHomepage.HOMEPAGE_SPECIAL_CONSTANTS

    fun shouldSkipWidgetInjection(spaceView: ObjectWrapper.SpaceView?): Boolean =
        isExplicitObjectHomepage(spaceView?.homepage)
}
