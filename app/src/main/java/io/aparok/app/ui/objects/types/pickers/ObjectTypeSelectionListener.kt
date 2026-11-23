package io.aparok.app.ui.objects.types.pickers

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper

interface ObjectTypeSelectionListener {
    fun onSelectObjectType(objType: ObjectWrapper.Type)
}

interface ObjectTypeUpdateListener {
    fun onUpdateObjectType(objType: ObjectWrapper.Type, fromFeatured: Boolean = false)
}

interface WidgetObjectTypeListener {
    fun onCreateWidgetObject(objType: ObjectWrapper.Type, widgetId: Id, source: Id)
}

interface WidgetSourceTypeListener {
    fun onSetNewWidgetSource(objType: ObjectWrapper.Type, widgetId: Id)
}

interface CollectionObjectTypeSelectionListener {
    fun onSelectObjectTypeForCollection(objType: ObjectWrapper.Type)
}