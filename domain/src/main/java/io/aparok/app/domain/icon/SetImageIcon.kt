package io.aparok.app.domain.icon

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase

abstract class SetImageIcon<T> : BaseUseCase<Pair<Payload, Hash>, SetImageIcon.Params<T>>() {
    /**
     * Params for for setting document's image icon
     * @property path image path in file system
     * @property target i.e. id of the context or text blockId
     */
    data class Params<T>(
        val target: T,
        val path: String,
        val spaceId: SpaceId
    )
}