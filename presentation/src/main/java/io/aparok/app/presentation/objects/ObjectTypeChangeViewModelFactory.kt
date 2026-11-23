package io.aparok.app.presentation.objects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import javax.inject.Inject

class ObjectTypeChangeViewModelFactory @Inject constructor(
    private val vmParams: ObjectTypeChangeViewModel.VmParams,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val dispatchers: AppCoroutineDispatchers,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectTypeChangeViewModel(
            vmParams = vmParams,
            storeOfObjectTypes = storeOfObjectTypes,
            dispatchers = dispatchers,
            spaceViews = spaceViews
        ) as T
    }
}