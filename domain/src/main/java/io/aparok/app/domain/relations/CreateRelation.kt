package io.aparok.app.domain.relations

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relation.Format.OBJECT
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Struct
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.objects.StoreOfRelations

/**
 * Use-case for creating a new relation object.
 */
class CreateRelation(
    private val repo: BlockRepository,
    private val storeOfRelations: StoreOfRelations
) : BaseUseCase<ObjectWrapper.Relation, CreateRelation.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.createRelation(
            space = params.space,
            name = params.name,
            format = params.format,
            formatObjectTypes = params.limitObjectTypes,
            prefilled = params.prefilled
        ).also {
            // Workaround for making sure that our relations store contains new relation
            // Before client code process this use-case's response.
            storeOfRelations.merge(
                listOf(it)
            )
        }
    }

    /**
     * @param [name] name of the new relation
     * @param [limitObjectTypes] limit object types provided for [OBJECT] format.
     * @param [format] format of the new relation
     * @param [prefilled] (optional) pre-filled values for this relation
     */
    class Params(
        val space: Id,
        val name: String,
        val format: RelationFormat,
        val limitObjectTypes: List<Id>,
        val prefilled: Struct,
    )
}