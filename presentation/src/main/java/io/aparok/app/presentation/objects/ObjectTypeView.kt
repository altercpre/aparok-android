package io.aparok.app.presentation.objects

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.ObjectTypeIds.COLLECTION
import io.aparok.app.core_models.ObjectTypeIds.NOTE
import io.aparok.app.core_models.ObjectTypeIds.PAGE
import io.aparok.app.core_models.ObjectTypeIds.TASK
import io.aparok.app.core_models.ui.ObjectIcon

data class ObjectTypeView(
    val id: Id,
    val key: Key,
    val name: String,
    val description: String?,
    val isSelected: Boolean = false,
    val defaultTemplate: Id? = null,
    val sourceObject: Id? = null,
    val icon: ObjectIcon
)

class ObjectTypeViewComparator : Comparator<ObjectTypeView> {

    /**
     * Sort object types by PAGE, NOTE, COLLECTION, TASK ids and others
     */
    override fun compare(o1: ObjectTypeView, o2: ObjectTypeView): Int {
        val o1Key = o1.key
        val o2Key = o2.key

        if (o1Key == o2Key) return 0

        if (o1Key == PAGE && o2Key != PAGE) return -1
        if (o1Key != PAGE && o2Key == PAGE) return 1

        if (o1Key == NOTE && o2Key != NOTE) return -1
        if (o1Key != NOTE && o2Key == NOTE) return 1

        if (o1Key == TASK && o2Key != TASK) return -1
        if (o1Key != TASK && o2Key == TASK) return 1

        if (o1Key == COLLECTION && o2Key != COLLECTION) return -1
        if (o1Key != COLLECTION && o2Key == COLLECTION) return 1

        val o1Name = o1.name
        val o2Name = o2.name

        return o1Name.compareTo(o2Name)
    }
}
