package io.aparok.app.presentation.editor.editor.ext

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Block.Content
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ext.content
import io.aparok.app.core_models.ext.replaceRangeWithWord
import io.aparok.app.core_models.ext.title
import io.aparok.app.core_models.restrictions.ObjectRestriction
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.BuildConfig
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.presentation.editor.editor.Markup
import io.aparok.app.presentation.extension.getDateObject
import io.aparok.app.presentation.extension.getObject
import io.aparok.app.presentation.extension.shift
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import timber.log.Timber

fun Block.Content.Text.getTextAndMarks(
    details: ObjectViewDetails,
    marks: List<Markup.Mark>,
    fieldParser: FieldParser,
    resourceProvider: ResourceProvider
): Pair<String, List<Markup.Mark>> {

    if (marks.none { it is Markup.Mark.Mention }) {
        return text to marks
    }
    var updatedText = text
    val updatedMarks = marks.toMutableList()
    updatedMarks.sortBy { it.from }
    try {
        updatedMarks.forEach { mark ->
            if (mark !is Markup.Mark.Mention || mark.param.isBlank()) return@forEach
            val newName = when (mark) {
                is Markup.Mark.Mention.Date -> getFormattedDateMention(
                    mark = mark,
                    details = details,
                    fieldParser = fieldParser,
                    resourceProvider = resourceProvider
                )
                is Markup.Mark.Mention.Deleted -> resourceProvider.getNonExistentObjectTitle()
                else -> {
                    val obj = details.getObject(mark.param)
                    if (obj != null) {
                        fieldParser.getObjectNameOrPluralsForTypes(obj)
                    } else {
                        return@forEach
                    }
                }
            }
            val oldName = updatedText.substring(mark.from, mark.to)
            val finalName =
                if (newName.isNullOrBlank()) resourceProvider.getUntitledTitle() else newName

            if (finalName != oldName) {
                val lengthDifference = finalName.length - oldName.length
                updatedText = updatedText.replaceRangeWithWord(
                    replace = finalName,
                    from = mark.from,
                    to = mark.to
                )
                updatedMarks.shift(
                    start = mark.from,
                    length = lengthDifference
                )
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Error while update mention markups")
        return text to marks
    }
    return updatedText to updatedMarks
}

private fun Block.Content.Text.getFormattedDateMention(
    mark: Markup.Mark.Mention.Date,
    details: ObjectViewDetails,
    fieldParser: FieldParser,
    resourceProvider: ResourceProvider
): String? {
    val dateObject = details.getDateObject(id = mark.param)
    return if (BuildConfig.ENABLE_RELATIVE_DATES_IN_MENTIONS) {
        val timestamp = dateObject?.timestamp
        val relativeDate = fieldParser.toDate(timestamp)?.relativeDate
        resourceProvider.toFormattedString(relativeDate = relativeDate).takeIf { it.isNotEmpty() }
    } else {
        dateObject?.name
    }
}

fun List<Block>.isAllowedToShowTypesWidget(
    objectRestrictions: List<ObjectRestriction>,
    isOwnerOrEditor: Boolean,
    objectLayout: ObjectType.Layout?
): Boolean {
    if (objectRestrictions.any { it == ObjectRestriction.TYPE_CHANGE }) return false
    if (!isOwnerOrEditor) return false
    return if (objectLayout == ObjectType.Layout.NOTE) {
        return true
    } else {
        return title()?.content<Content.Text>()?.text?.isEmpty() == true
    }
}

