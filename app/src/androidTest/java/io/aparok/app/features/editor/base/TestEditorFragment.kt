package io.aparok.app.features.editor.base

import android.os.Bundle
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import io.aparok.app.R
import io.aparok.app.presentation.editor.EditorViewModelFactory
import io.aparok.app.ui.editor.EditorFragment
import org.mockito.kotlin.mock

class TestEditorFragment : EditorFragment() {

    lateinit var navController: TestNavHostController

    init {
        factory = testViewModelFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
    }

    private fun setupNavController() {
        navController = TestNavHostController(requireContext())
        val viewModelStore = ViewModelStore()
        navController.setViewModelStore(viewModelStore)
        navController.setGraph(R.navigation.graph)
        navController.setCurrentDestination(R.id.pageScreen)
        viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                Navigation.setViewNavController(this.requireView(), navController)
            }
        }
    }

    companion object {
        lateinit var testViewModelFactory: EditorViewModelFactory
    }
}