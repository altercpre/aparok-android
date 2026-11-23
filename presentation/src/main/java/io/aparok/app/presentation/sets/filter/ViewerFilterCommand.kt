package io.aparok.app.presentation.sets.filter

import io.aparok.app.core_models.Id

sealed class ViewerFilterCommand {

    sealed class Modal : ViewerFilterCommand() {

        data class ShowRelationList(val viewer: Id) : Modal()

        data class UpdateInputValueFilter(
            val relation: Id,
            val filterIndex: Int
        ) : Modal()

        data class UpdateSelectValueFilter(
            val relation: Id,
            val filterIndex: Int
        ) : Modal()
    }

    /**
     * Command to show a message indicating that the filter is advanced
     * and can only be edited on Desktop.
     */
    data object ShowAdvancedFilterMessage : ViewerFilterCommand()
}