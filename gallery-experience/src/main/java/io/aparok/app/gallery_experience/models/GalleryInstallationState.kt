package io.aparok.app.gallery_experience.models

import io.aparok.app.core_models.ManifestInfo
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.ui.SpaceIconView


sealed class GalleryInstallationState {
    object Hidden : GalleryInstallationState()
    object Loading : GalleryInstallationState()
    data class Success(val info: ManifestInfo, val isLoading: Boolean = false) : GalleryInstallationState()
}

data class GalleryInstallationSpacesState(
    val spaces: List<GallerySpaceView>,
    val isNewButtonVisible: Boolean
)

sealed class GalleryInstallationNavigation(val route: String) {
    object Main : GalleryInstallationNavigation("main")
    object Spaces : GalleryInstallationNavigation("spaces")
    object CloseSpaces : GalleryInstallationNavigation("closeSpaces")
    object Dismiss : GalleryInstallationNavigation("")
}

data class GallerySpaceView(
    val obj: ObjectWrapper.SpaceView,
    val icon: SpaceIconView
)