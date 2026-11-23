package io.aparok.app.presentation.widgets

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.WidgetLayout
import io.aparok.app.core_models.isDataView
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.Resultat
import io.aparok.app.domain.base.fold
import io.aparok.app.domain.base.onSuccess
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.widgets.CreateWidget
import io.aparok.app.domain.widgets.DeleteWidget
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.common.PayloadDelegator
import javax.inject.Inject
import timber.log.Timber

interface PinObjectAsWidgetDelegate {

    suspend fun pinChat(space: SpaceId, obj: Id) : Resultat<Any>
    suspend fun unpinChat(space: SpaceId, obj: Id) : Resultat<Any>
    suspend fun isChatPinned(space: SpaceId, obj: Id) : Boolean

    class Default @Inject constructor(
        private val createWidget: CreateWidget,
        private val deleteWidget: DeleteWidget,
        private val showObject: GetObject,
        private val spaceManager: SpaceManager,
        private val payloadDelegator: PayloadDelegator
    ) : PinObjectAsWidgetDelegate {

        override suspend fun pinChat(
            space: SpaceId,
            obj: Id
        ): Resultat<Any> {

            val config = spaceManager.getConfig(space)
            if (config == null)
                return Resultat.Failure(Exception("Space config not found"))

            // Check if the chat is already pinned
            val existingWidgetId = findWidgetForChat(config.widgets, SpaceId(config.space), obj)
            if (existingWidgetId != null) {
                Timber.d("Chat is already pinned as widget: $existingWidgetId")
                return Resultat.Success(Unit)
            }

            var target: Id? = null
            showObject.async(
                GetObject.Params(
                    target = config.widgets,
                    space = SpaceId(config.space),
                    saveAsLastOpened = false
                )
            ).onSuccess { objectView ->
                objectView.blocks.find { it.content is Block.Content.Widget }?.let {
                    target = it.id
                }
            }

            val params = CreateWidget.Params(
                ctx = config.widgets,
                source = obj,
                type = WidgetLayout.LINK,
                position = Position.TOP,
                target = target
            )
            return createWidget.async(params).also { result ->
                // Dispatch payload to HomeScreenViewModel
                result.onSuccess { payload ->
                    payloadDelegator.dispatch(payload)
                }
            }
        }

        override suspend fun unpinChat(
            space: SpaceId,
            obj: Id
        ): Resultat<Any> {
            val config = spaceManager.getConfig(space)
            if (config == null)
                return Resultat.Failure(Exception("Space config not found"))

            // Find the widget ID for this chat
            val widgetId = findWidgetForChat(config.widgets, SpaceId(config.space), obj)
            if (widgetId == null) {
                Timber.d("No widget found for chat: $obj")
                return Resultat.Success(Unit)
            }

            // Delete the widget
            return deleteWidget.async(
                DeleteWidget.Params(
                    ctx = config.widgets,
                    targets = listOf(widgetId)
                )
            ).also { result ->
                // Dispatch payload to HomeScreenViewModel
                result.onSuccess { payload ->
                    payloadDelegator.dispatch(payload)
                }
            }
        }

        override suspend fun isChatPinned(
            space: SpaceId,
            obj: Id
        ): Boolean {
            val config = spaceManager.getConfig(space) ?: return false
            return findWidgetForChat(config.widgets, SpaceId(config.space), obj) != null
        }

        private suspend fun findWidgetForChat(
            widgetsObjectId: Id,
            space: SpaceId,
            chatId: Id
        ): Id? {
            var foundWidgetId: Id? = null
            showObject.async(
                GetObject.Params(
                    target = widgetsObjectId,
                    space = space,
                    saveAsLastOpened = false
                )
            ).onSuccess { objectView ->
                // Find all widget blocks
                objectView.blocks.forEach { block ->
                    if (block.content is Block.Content.Widget) {
                        // Get children of this widget block
                        val children = objectView.blocks.filter { it.id in block.children }
                        // Check if any child is a Link to our chat
                        children.forEach { child ->
                            val linkContent = child.content as? Block.Content.Link
                            if (linkContent?.target == chatId) {
                                foundWidgetId = block.id
                                return@onSuccess
                            }
                        }
                    }
                }
            }
            return foundWidgetId
        }
    }

}
