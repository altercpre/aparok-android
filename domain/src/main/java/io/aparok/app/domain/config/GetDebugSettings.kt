package io.aparok.app.domain.config

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either

class GetDebugSettings(private val repo: InfrastructureRepository) :
    BaseUseCase<DebugSettings, Unit>() {

    override suspend fun run(params: Unit): Either<Throwable, DebugSettings> = safe {
        DebugSettings(isAnytypeContextMenuEnabled = false)
    }
}