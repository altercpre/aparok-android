package io.aparok.app.domain.templates

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectTypeIds
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.workspace.SpaceManager
import kotlinx.coroutines.withContext

/**
 * Use-case for getting templates for specific [ObjectType]
 */
class GetTemplates(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetTemplates.Params, List<ObjectWrapper.Basic>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<ObjectWrapper.Basic> {
        return withContext(dispatchers.io) {
            try {
                repo.searchObjects(
                    // TODO provide space id to params
                    space = SpaceId(spaceManager.get()),
                    filters = listOf(
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
                            relation = Relations.TARGET_OBJECT_TYPE,
                            condition = DVFilterCondition.EQUAL,
                            value = params.type.id
                        ),
                        DVFilter(
                            relation = Relations.TYPE_UNIQUE_KEY,
                            condition = DVFilterCondition.EQUAL,
                            value = ObjectTypeIds.TEMPLATE
                        ),
                        DVFilter(
                            relation = Relations.ID,
                            condition = DVFilterCondition.NOT_EMPTY
                        )
                    ),
                    keys = listOf(
                        Relations.ID,
                        Relations.SPACE_ID,
                        Relations.NAME,
                        Relations.PLURAL_NAME,
                        Relations.LAYOUT,
                        Relations.ICON_EMOJI,
                        Relations.ICON_NAME,
                        Relations.ICON_IMAGE,
                        Relations.ICON_OPTION,
                        Relations.COVER_ID,
                        Relations.COVER_TYPE
                    ),
                    sorts = listOf(
                        DVSort(
                            relationKey = Relations.CREATED_DATE,
                            type = Block.Content.DataView.Sort.Type.DESC,
                            relationFormat = RelationFormat.DATE
                        )
                    ),
                    fulltext = "",
                    offset = 0,
                    limit = 100
                ).map { obj ->
                    ObjectWrapper.Basic(obj)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * @property [type] id of the object type, whose templates we are interested in.
     */
    data class Params(val type: TypeId)
}