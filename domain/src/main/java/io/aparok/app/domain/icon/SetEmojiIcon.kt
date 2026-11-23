package io.aparok.app.domain.icon

import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase

abstract class SetEmojiIcon<T> :
    BaseUseCase<Payload, SetEmojiIcon.Params<T>>() {

    /**
     * @property emoji emoji's unicode
     * @property target i.e. object or text block
     */
    data class Params<T>(
        val target: T,
        val emoji: String
    )
}