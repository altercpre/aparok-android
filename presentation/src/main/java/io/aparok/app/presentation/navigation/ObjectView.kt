package io.aparok.app.presentation.navigation

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ui.ObjectIcon

interface DefaultSearchItem

data object NewObject: DefaultSearchItem

data object SectionDates: DefaultSearchItem
data object SelectDateItem: DefaultSearchItem
data object SectionObjects: DefaultSearchItem

data class DefaultObjectView(
    val id: Id,
    val space: Id,
    val name: String,
    val type: String? = null,
    val typeName: String? = null,
    val description: String? = null,
    val layout: ObjectType.Layout? = null,
    val icon: ObjectIcon = ObjectIcon.None,
    val lastModifiedDate: Long = 0L,
    val lastOpenedDate: Long = 0L,
    val isFavorite: Boolean = false
) : DefaultSearchItem
