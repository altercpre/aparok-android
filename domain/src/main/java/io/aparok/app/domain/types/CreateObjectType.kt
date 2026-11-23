package io.aparok.app.domain.types

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Relations
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class CreateObjectType(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateObjectType.Params, String>(dispatchers.io) {
    override suspend fun doWork(params: Params): String {
        val command = Command.CreateObjectType(
            details = mapOf(
                Relations.NAME to params.name,
                Relations.ICON_NAME to params.iconName,
                Relations.PLURAL_NAME to params.pluralName,
                Relations.ICON_OPTION to params.iconColor
            ),
            spaceId = params.space,
            internalFlags = listOf()
        )
        return repo.createType(command)
    }

    class Params(
        val space: Id,
        val name: String,
        val iconName: String,
        val pluralName: String,
        val iconColor: Double?
    )
}