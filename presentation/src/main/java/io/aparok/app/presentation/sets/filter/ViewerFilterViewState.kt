package io.aparok.app.presentation.sets.filter

import io.aparok.app.presentation.sets.model.FilterView

sealed class ViewerFilterViewState {

    object Init : ViewerFilterViewState()
    object NoFields : ViewerFilterViewState()
    data class Success(val items: List<FilterView>) : ViewerFilterViewState()
    data class Error(val msg: String) : ViewerFilterViewState()
}