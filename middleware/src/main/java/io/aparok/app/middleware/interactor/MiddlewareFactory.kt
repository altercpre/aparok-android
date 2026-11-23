package io.aparok.app.middleware.interactor

import io.aparok.app.core_models.Block
import io.aparok.app.middleware.mappers.MBBookmark
import io.aparok.app.middleware.mappers.MBDiv
import io.aparok.app.middleware.mappers.MBDivStyle
import io.aparok.app.middleware.mappers.MBFile
import io.aparok.app.middleware.mappers.MBLink
import io.aparok.app.middleware.mappers.MBMarks
import io.aparok.app.middleware.mappers.MBRelation
import io.aparok.app.middleware.mappers.MBTableOfContents
import io.aparok.app.middleware.mappers.MBText
import io.aparok.app.middleware.mappers.MBlock
import io.aparok.app.middleware.mappers.MBookmarkState
import io.aparok.app.middleware.mappers.toMiddlewareModel

class MiddlewareFactory {

    fun create(prototype: Block.Prototype): MBlock {
        return when (prototype) {
            is Block.Prototype.Bookmark.New -> {
                val bookmark = MBBookmark()
                MBlock(bookmark = bookmark)
            }
            is Block.Prototype.Bookmark.Existing -> {
                val bookmark = MBBookmark(
                    targetObjectId = prototype.target,
                    state = MBookmarkState.Done
                )
                MBlock(bookmark = bookmark)
            }
            is Block.Prototype.Text -> {
                val text = MBText(
                    style = prototype.style.toMiddlewareModel(),
                    text = prototype.text.orEmpty(),
                    marks = if (prototype.marks.isEmpty()) {
                        null
                    } else {
                        MBMarks(marks = prototype.marks.map { it.toMiddlewareModel() })
                    },
                    checked = prototype.checked ?: false,
                    color = prototype.color.orEmpty(),
                    iconEmoji = prototype.iconEmoji.orEmpty(),
                    iconImage = prototype.iconImage.orEmpty()
                )
                MBlock(
                    text = text,
                    backgroundColor = prototype.backgroundColor.orEmpty(),
                    align = prototype.align.toMiddlewareModel(),
                    fields = prototype.fields
                        ?.takeIf { it.map.isNotEmpty() }
                        ?.toMiddlewareModel()
                )
            }
            is Block.Prototype.DividerLine -> {
                val divider = MBDiv(style = MBDivStyle.Line)
                MBlock(div = divider)
            }
            is Block.Prototype.DividerDots -> {
                val divider = MBDiv(style = MBDivStyle.Dots)
                MBlock(div = divider)
            }
            is Block.Prototype.File -> {
                val file = MBFile(
                    state = prototype.state.toMiddlewareModel(),
                    type = prototype.type.toMiddlewareModel(),
                    targetObjectId = prototype.targetObjectId.orEmpty()
                )
                MBlock(file_ = file)
            }
            is Block.Prototype.Link -> {
                val link = MBLink(
                    targetBlockId = prototype.target,
                    cardStyle = prototype.cardStyle.toMiddlewareModel(),
                    iconSize = prototype.iconSize.toMiddlewareModel(),
                    description = prototype.description.toMiddlewareModel()
                )
                MBlock(link = link)
            }
            is Block.Prototype.Relation -> {
                val relation = MBRelation(
                    key = prototype.key
                )
                MBlock(relation = relation)
            }
            is Block.Prototype.TableOfContents -> {
                val toc = MBTableOfContents()
                MBlock(tableOfContents = toc)
            }
            else -> throw IllegalStateException("Unexpected prototype: $prototype")
        }
    }
}