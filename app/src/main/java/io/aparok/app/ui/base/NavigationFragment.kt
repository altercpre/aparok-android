package io.aparok.app.ui.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import io.aparok.app.BuildConfig
import io.aparok.app.core_utils.common.EventWrapper
import io.aparok.app.core_utils.ui.BaseFragment
import io.aparok.app.presentation.navigation.AppNavigation
import io.aparok.app.presentation.navigation.AppNavigation.Command
import timber.log.Timber

@Deprecated("Use NavigationRouter directly")
abstract class NavigationFragment<BINDING : ViewBinding>(
    @LayoutRes private val layout: Int
) : BaseFragment<BINDING>(layout) {

    private val navigationRouter by lazy {
        NavigationRouter((requireActivity() as AppNavigation.Provider).nav())
    }

    val navObserver = Observer<EventWrapper<Command>> { event ->
        event.getContentIfNotHandled()?.let {
            try {
                throttle { navigationRouter.navigate(it) }
            } catch (e: Exception) {
                Timber.e(e, "Navigation: $it")
                if (BuildConfig.DEBUG) {
                    throw e
                }
            }
        }
    }
}