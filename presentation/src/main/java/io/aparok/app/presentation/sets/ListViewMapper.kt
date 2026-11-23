package io.aparok.app.presentation.sets

import io.aparok.app.core_models.DVViewer
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.getTypeOfObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.core_models.ui.objectIcon
import io.aparok.app.presentation.objects.relationsFilteredByHiddenAndDescription
import io.aparok.app.presentation.objects.setTypeRelationIconsAsNone
import io.aparok.app.presentation.sets.model.Viewer

suspend fun DVViewer.buildListViews(
    objects: List<Id>,
    relations: List<ObjectWrapper.Relation>,
    urlBuilder: UrlBuilder,
    store: ObjectStore,
    objectOrderIds: List<Id>,
    fieldParser: FieldParser,
    storeOfObjectTypes: StoreOfObjectTypes
): List<Viewer.ListView.Item> {
    val items = objects.mapNotNull { id ->
        val obj = store.get(id)
        if (obj != null && obj.isValid) {
            when (obj.layout) {
                ObjectType.Layout.PROFILE -> {
                    val description = if (relations.any { it.key == Relations.DESCRIPTION }) {
                        obj.description
                    } else {
                        null
                    }
                    Viewer.ListView.Item.Profile(
                        objectId = obj.id,
                        relations = obj.relationsFilteredByHiddenAndDescription(
                            relations = relations,
                            urlBuilder = urlBuilder,
                            settings = viewerRelations,
                            storeOfObjects = store,
                            fieldParser = fieldParser,
                            storeOfObjectTypes = storeOfObjectTypes
                        ).setTypeRelationIconsAsNone(),
                        name = fieldParser.getObjectName(obj),
                        icon = obj.objectIcon(
                            builder = urlBuilder,
                            objType = storeOfObjectTypes.getTypeOfObject(obj)
                        ),
                        description = description,
                        hideIcon = hideIcon
                    )
                }
                ObjectType.Layout.TODO -> {
                    val description = if (relations.any { it.key == Relations.DESCRIPTION }) {
                        obj.description
                    } else {
                        null
                    }
                    Viewer.ListView.Item.Task(
                        objectId = obj.id,
                        relations = obj.relationsFilteredByHiddenAndDescription(
                            relations = relations,
                            urlBuilder = urlBuilder,
                            settings = viewerRelations,
                            storeOfObjects = store,
                            fieldParser = fieldParser,
                            storeOfObjectTypes = storeOfObjectTypes
                        ).setTypeRelationIconsAsNone(),
                        name = fieldParser.getObjectName(obj),
                        done = obj.done == true,
                        description = description
                    )
                }
                else -> {
                    val description = if (relations.any { it.key == Relations.DESCRIPTION }) {
                        obj.description
                    } else {
                        null
                    }
                    Viewer.ListView.Item.Default(
                        objectId = obj.id,
                        relations = obj.relationsFilteredByHiddenAndDescription(
                            relations = relations,
                            urlBuilder = urlBuilder,
                            settings = viewerRelations,
                            storeOfObjects = store,
                            fieldParser = fieldParser,
                            storeOfObjectTypes = storeOfObjectTypes
                        ).setTypeRelationIconsAsNone(),
                        name = fieldParser.getObjectName(obj),
                        icon = obj.objectIcon(
                            builder = urlBuilder,
                            objType = storeOfObjectTypes.getTypeOfObject(obj)
                        ),
                        description = description,
                        hideIcon = hideIcon
                    )
                }
            }
        } else {
            null
        }
    }
    return if (objectOrderIds.isNotEmpty()) {
        items.sortObjects(objectOrderIds)
    } else {
        items
    }
}

private fun List<Viewer.ListView.Item>.sortObjects(objectOrderIds: List<Id>): List<Viewer.ListView.Item> {
    val orderMap = objectOrderIds.mapIndexed { index, id -> id to index }.toMap()
    return sortedBy { orderMap[it.objectId] }
}