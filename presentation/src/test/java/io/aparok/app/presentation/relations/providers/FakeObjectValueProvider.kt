package io.aparok.app.presentation.relations.providers

import io.aparok.app.core_models.Id
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FakeObjectValueProvider(
    var values: Map<Id, Map<String, Any?>> = emptyMap()
) : ObjectValueProvider {

    override suspend fun get(ctx: Id, target: Id): Map<String, Any?> = values[target] ?: emptyMap()

    override suspend fun subscribe(ctx: Id, target: Id): Flow<Map<String, Any?>> =
        flow {
            emit(
                get(ctx = ctx, target = target)
            )
        }
}