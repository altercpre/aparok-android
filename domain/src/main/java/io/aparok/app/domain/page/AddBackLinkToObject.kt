package io.aparok.app.domain.page

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.interactor.CreateBlock

/**
 * Add backlink from set or object itself to another object as a last block
 */
class AddBackLinkToObject(
    private val openPage: OpenPage,
    private val createBlock: CreateBlock,
    private val closeObject: CloseObject,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddBackLinkToObject.Params, ObjectWrapper.Basic>(dispatchers.io) {


    override suspend fun doWork(params: Params): ObjectWrapper.Basic {
        val openPageParams = OpenPage.Params(
            obj = params.objectToPlaceLink,
            saveAsLastOpened = params.saveAsLastOpened,
            space = params.spaceId
        )
        when (val result = openPage.run(openPageParams)) {
            is Result.Success -> {
                val event = result.data
                    .events
                    .firstOrNull { event -> event is Event.Command.ShowObject }

                check(event is Event.Command.ShowObject) { "Event ShowObject is missing" }

                val targetBlock = event.blocks
                    .firstOrNull { it.id == params.objectToPlaceLink }
                    ?.children
                    ?.last()

                val objectDetails = event.details[params.objectToPlaceLink]

                require(targetBlock != null) { "Target block is missing" }
                require(objectDetails != null) { "Object details is missing" }

                createBlock.run(
                    CreateBlock.Params(
                        context = params.objectToPlaceLink,
                        target = targetBlock,
                        position = Position.BOTTOM,
                        prototype = Block.Prototype.Link(target = params.objectToLink)
                    )
                )

                closeObject.run(CloseObject.Params(params.objectToPlaceLink, params.spaceId))

                return ObjectWrapper.Basic(objectDetails)
            }
            is Result.Failure -> throw IllegalStateException("object open error ${result.error}")
        }
    }

    data class Params(
        val objectToLink: Id,
        val objectToPlaceLink: Id,
        val saveAsLastOpened: Boolean,
        val spaceId: SpaceId
    )
}