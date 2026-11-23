package io.aparok.app.presentation.relations.providers

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Struct
import kotlinx.coroutines.flow.Flow

interface ObjectValueProvider {
    suspend fun get(ctx: Id, target: Id): Struct
    suspend fun subscribe(ctx: Id, target: Id) : Flow<Struct>
}