package io.aparok.app.domain.page

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.InternalFlags
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.workspace.SpaceManager

/**
 * UseCase for creating a new object as mention or as text link markup
 */

class CreateObjectAsMentionOrLink(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateObjectAsMentionOrLink.Params, CreateObjectAsMentionOrLink.Result>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Result {

        val space = SpaceId(spaceManager.get())

        val typeKey = params.typeKey

        val prefilled = buildMap {
            put(Relations.NAME, params.name)
        }

        val command = Command.CreateObject(
            template = params.defaultTemplate,
            prefilled = prefilled,
            internalFlags = listOf(InternalFlags.ShouldSelectTemplate, InternalFlags.ShouldSelectType),
            space = space,
            typeKey = typeKey
        )
        val result = repo.createObject(command)

        return Result(
            id = result.id,
            name = params.name
        )
    }

    /**
     * [name] name for new object
     * [type] type for new object
     */
    data class Params(
        val name: String,
        val typeKey: TypeKey,
        val defaultTemplate: String? = null
    )

    data class Result(
        val id: String,
        val name: String
    )
}