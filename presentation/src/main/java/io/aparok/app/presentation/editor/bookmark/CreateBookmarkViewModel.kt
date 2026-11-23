package io.aparok.app.presentation.editor.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.ui.ViewStateViewModel
import io.aparok.app.presentation.editor.bookmark.CreateBookmarkViewModel.ViewState

class CreateBookmarkViewModel() : ViewStateViewModel<ViewState>() {

    fun onCreateBookmarkClicked(
        url: String
    ) {
        update(ViewState.Success(url = url))
    }

    sealed class ViewState {
        data class Success(val url: String) : ViewState()
        data class Error(val message: String) : ViewState()
        data object Exit : ViewState()
    }

    class Factory() : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CreateBookmarkViewModel() as T
    }
}