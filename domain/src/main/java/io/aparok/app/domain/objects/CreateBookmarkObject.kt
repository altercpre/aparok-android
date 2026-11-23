package io.aparok.app.domain.objects

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.Url
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for creating bookmark object from url.
 */
class CreateBookmarkObject @Inject constructor(
    private val repo: BlockRepository
) : BaseUseCase<Id, CreateBookmarkObject.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.createBookmarkObject(
            space = params.space,
            url = params.url,
            details = params.details,
            createdInContext = params.createdInContext
        )
    }

    data class Params(
        val space: Id,
        val url: Url,
        val details: Struct = emptyMap(),
        val createdInContext: Id? = null
    )
}
