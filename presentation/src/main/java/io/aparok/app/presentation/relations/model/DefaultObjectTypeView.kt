package io.aparok.app.presentation.relations.model

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ui.ObjectIcon

data class DefaultObjectTypeView(
    val id: Id,
    val title: String,
    val subtitle: String?,
    val icon: ObjectIcon
)

data class SelectLimitObjectTypeView(
    val item: DefaultObjectTypeView,
    val isSelected: Boolean
)

data class LimitObjectTypeValueView(val types: List<DefaultObjectTypeView>)