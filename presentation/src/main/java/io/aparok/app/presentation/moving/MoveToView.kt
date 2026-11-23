package io.aparok.app.presentation.moving

import io.aparok.app.presentation.navigation.DefaultObjectView

sealed class MoveToView {
    object Init : MoveToView()
    object Loading : MoveToView()
    object EmptyPages : MoveToView()
    data class Success(val objects: List<DefaultObjectView>) : MoveToView()
    data class NoResults(val searchText: String) : MoveToView()
    data class Error(val error: String) : MoveToView()
}