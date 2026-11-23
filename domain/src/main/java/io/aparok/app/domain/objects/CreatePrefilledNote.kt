package io.aparok.app.domain.objects

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NO_VALUE
import io.aparok.app.core_models.ObjectTypeUniqueKeys
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreatePrefilledNote @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreatePrefilledNote.Params, Id>(dispatchers.io) {

    override suspend fun doWork(params: Params): Id {
        val obj = repo.createObject(
            Command.CreateObject(
                typeKey = params.customType ?: TypeKey(ObjectTypeUniqueKeys.NOTE),
                space = SpaceId(params.space),
                template = null,
                internalFlags = emptyList(),
                prefilled = params.details
            )
        )
        repo.create(
            command = Command.Create(
                context = obj.id,
                prototype = Block.Prototype.Text(
                    style = Block.Content.Text.Style.P,
                    text = params.text
                ),
                position = Position.NONE,
                target = NO_VALUE
            )
        )
        params.attachments.forEach { attachment ->
            repo.create(
                command = Command.Create(
                    context = obj.id,
                    prototype = Block.Prototype.Link(
                        target = attachment,
                        cardStyle = Block.Content.Link.CardStyle.CARD
                    ),
                    position = Position.NONE,
                    target = NO_VALUE
                )
            )
        }
        return obj.id
    }

    /**
     * @param [customType] provide custom type instead of note if needed
     */
    data class Params(
        val space: Id,
        val text: String,
        val details: Struct,
        val customType: TypeKey? = null,
        val attachments: List<Id> = emptyList()
    )
}