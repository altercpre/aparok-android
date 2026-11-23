package io.aparok.app.domain.launch

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NO_VALUE
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectTypeIds.DEFAULT_OBJECT_TYPE
import io.aparok.app.core_models.ObjectTypeUniqueKeys
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.ext.mapToObjectWrapperType
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.core_models.restrictions.ObjectRestriction
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetDefaultObjectType @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    private val blockRepository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, GetDefaultObjectType.Response>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId): Response {
        val defaultType = userSettingsRepository.getDefaultObjectType(params)
        if (defaultType != null) {
            val item = searchObjectByIdAndSpaceId(
                type = defaultType,
                space = params
            )
            return if (item != null) {
                val key = TypeKey(item.uniqueKey)
                val id = TypeId(item.id)
                Response(
                    type = key,
                    name = item.name,
                    id = id,
                    defaultTemplate = item.defaultTemplateId
                )
            } else {
                fetchFallbackObjectType(params)
            }
        } else {
            return fetchFallbackObjectType(params)
        }
    }

    private suspend fun fetchFallbackObjectType(space: SpaceId): Response {
        val structs = blockRepository.searchObjects(
            space = space,
            limit = 1,
            fulltext = NO_VALUE,
            filters = buildList {
                // TODO DROID-2916 might need to delete this filter
                add(
                    DVFilter(
                        relation = Relations.UNIQUE_KEY,
                        condition = DVFilterCondition.EQUAL,
                        value = DEFAULT_OBJECT_TYPE
                    )
                )
            },
            offset = 0,
            sorts = emptyList(),
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.PLURAL_NAME,
                Relations.UNIQUE_KEY,
                Relations.SPACE_ID,
                Relations.DEFAULT_TEMPLATE_ID
            )
        )
        val objType = structs.firstOrNull()?.mapToObjectWrapperType()
        if (objType != null) {
            val key = TypeKey(objType.uniqueKey)
            val id = TypeId(objType.id)
            return Response(
                type = key,
                name = objType.name,
                id = id,
                defaultTemplate = objType.defaultTemplateId
            )
        } else {
            throw IllegalStateException("Default type not found")
        }
    }

    private suspend fun searchObjectByIdAndSpaceId(
        type: TypeId,
        space: SpaceId
    ): ObjectWrapper.Type? {
        val structs = blockRepository.searchObjects(
            space = space,
            limit = 1,
            fulltext = NO_VALUE,
            filters = buildList {
                addAll(filterObjectTypeLibrary())
                add(
                    DVFilter(
                        relation = Relations.ID,
                        condition = DVFilterCondition.EQUAL,
                        value = type.id
                    )
                )
            },
            offset = 0,
            sorts = emptyList(),
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.PLURAL_NAME,
                Relations.UNIQUE_KEY,
                Relations.SPACE_ID,
                Relations.DEFAULT_TEMPLATE_ID
            )
        )
        return structs.firstOrNull()?.mapToObjectWrapperType()
    }

    private fun filterObjectTypeLibrary() = listOf(
        DVFilter(
            relation = Relations.LAYOUT,
            condition = DVFilterCondition.EQUAL,
            value = ObjectType.Layout.OBJECT_TYPE.code.toDouble()
        ),
        DVFilter(
            relation = Relations.IS_ARCHIVED,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_DELETED,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_HIDDEN,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_HIDDEN_DISCOVERY,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.RESTRICTIONS,
            condition = DVFilterCondition.NOT_IN,
            value = listOf(ObjectRestriction.CREATE_OBJECT_OF_THIS_TYPE.code.toDouble())
        )
    )

    data class Response(
        val id: TypeId,
        val type: TypeKey,
        val name: String?,
        val defaultTemplate: Id?
    )
}