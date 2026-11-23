package io.aparok.app.presentation.relations.model

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.RelativeDate
import io.aparok.app.presentation.sets.model.FileView
import io.aparok.app.presentation.sets.model.ObjectView
import io.aparok.app.presentation.sets.model.StatusView
import io.aparok.app.presentation.sets.model.TagView

sealed class DefaultObjectRelationValueView {

    abstract val relationKey: Id
    abstract val objectId: Id

    data class Text(
        override val objectId: Id,
        override val relationKey: Id,
        val text: String?
    ) : DefaultObjectRelationValueView()

    data class Number(
        override val objectId: Id,
        override val relationKey: Id,
        val number: String?
    ) : DefaultObjectRelationValueView()

    data class Url(
        override val objectId: Id,
        override val relationKey: Id,
        val url: String?
    ) : DefaultObjectRelationValueView()

    data class Email(
        override val objectId: Id,
        override val relationKey: Id,
        val email: String?
    ) : DefaultObjectRelationValueView()

    data class Phone(
        override val objectId: Id,
        override val relationKey: Id,
        val phone: String?
    ) : DefaultObjectRelationValueView()

    data class Checkbox(
        override val objectId: Id,
        override val relationKey: Id,
        val isChecked: Boolean
    ) : DefaultObjectRelationValueView()

    data class Date(
        override val objectId: Id,
        override val relationKey: Id,
        val timeInMillis: Long? = null,
        val relativeDate: RelativeDate?,
        val isTimeIncluded: Boolean,
    ) : DefaultObjectRelationValueView()

    data class Tag(
        override val objectId: Id,
        override val relationKey: Id,
        val tags: List<TagView>
    ) : DefaultObjectRelationValueView()

    data class Status(
        override val objectId: Id,
        override val relationKey: Id,
        val status: List<StatusView>
    ) : DefaultObjectRelationValueView()

    data class Object(
        override val objectId: Id,
        override val relationKey: Id,
        val objects: List<ObjectView>
    ) : DefaultObjectRelationValueView()

    data class File(
        override val objectId: Id,
        override val relationKey: Id,
        val files: List<FileView>
    ) : DefaultObjectRelationValueView()

    data class Empty(
        override val objectId: Id,
        override val relationKey: Id,
    ) : DefaultObjectRelationValueView()
}