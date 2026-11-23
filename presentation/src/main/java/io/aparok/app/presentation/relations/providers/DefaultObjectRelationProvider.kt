package io.aparok.app.presentation.relations.providers

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.editor.Editor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class DefaultObjectRelationProvider(
    private val storeOfRelations: StoreOfRelations,
    private val storage: Editor.Storage
) : ObjectRelationProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeAll(id: Id): Flow<List<ObjectWrapper.Relation>> =
        combine(
            storeOfRelations.trackChanges(),
            storage.details.stream()
        ) { _, details ->
            details
        }.flatMapLatest { details ->
            val objectKeys = details.details[id]?.keys.orEmpty()
            flow {
                objectKeys.mapNotNull {
                    storeOfRelations.getByKey(it)
                }
            }
        }
}