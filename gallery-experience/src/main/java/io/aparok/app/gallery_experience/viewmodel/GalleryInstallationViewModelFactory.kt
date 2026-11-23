package io.aparok.app.gallery_experience.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.gallery_experience.DownloadGalleryManifest
import io.aparok.app.domain.gallery_experience.ImportExperience
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.spaces.CreateSpace
import io.aparok.app.domain.spaces.GetSpaceViews
import io.aparok.app.domain.workspace.EventProcessImportChannel
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import javax.inject.Inject

class GalleryInstallationViewModelFactory @Inject constructor(
    private val viewModelParams: GalleryInstallationViewModel.ViewModelParams,
    private val downloadGalleryManifest: DownloadGalleryManifest,
    private val importExperience: ImportExperience,
    private val analytics: Analytics,
    private val getSpaceViews: GetSpaceViews,
    private val createSpace: CreateSpace,
    private val urlBuilder: UrlBuilder,
    private val spaceGradientProvider: SpaceGradientProvider,
    private val  userPermissionProvider: UserPermissionProvider,
    private val eventProcessChannel: EventProcessImportChannel,
    private val configStorage: ConfigStorage
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryInstallationViewModel(
            viewModelParams = viewModelParams,
            downloadGalleryManifest = downloadGalleryManifest,
            importExperience = importExperience,
            analytics = analytics,
            getSpaceViews = getSpaceViews,
            urlBuilder = urlBuilder,
            spaceGradientProvider = spaceGradientProvider,
            createSpace = createSpace,
            userPermissionProvider = userPermissionProvider,
            eventProcessChannel = eventProcessChannel,
            configStorage = configStorage
        ) as T
    }
}