package io.aparok.app.feature_os_widgets.mapper

import io.aparok.app.core_models.SystemColor
import io.aparok.app.feature_os_widgets.model.OsWidgetSpaceIcon
import io.aparok.app.feature_os_widgets.model.OsWidgetSpaceItem
import io.aparok.app.feature_os_widgets.persistence.OsWidgetSpaceEntity

/**
 * Maps persistence entity to domain/UI model.
 */
fun OsWidgetSpaceEntity.toDomain(): OsWidgetSpaceItem {
    val color = SystemColor.color(iconColorIndex)
    val imageUrl = iconImageUrl
    val icon = if (imageUrl != null) {
        OsWidgetSpaceIcon.Image(url = imageUrl, color = color)
    } else {
        OsWidgetSpaceIcon.Placeholder(color = color, name = name)
    }

    return OsWidgetSpaceItem(
        spaceId = spaceId,
        name = name,
        icon = icon,
        isOneToOneSpace = isOneToOneSpace
    )
}

/**
 * Maps a list of entities to domain models.
 */
fun List<OsWidgetSpaceEntity>.toDomain(): List<OsWidgetSpaceItem> = map { it.toDomain() }
