package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Url
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for reloading object content.
 * @see [Params]
 */
class ReloadObject(
    private val repo: BlockRepository
) : BaseUseCase<Unit, ReloadObject.Params>() {

    override suspend fun run(params: Params) = safe {
        when(params) {
            is Params.FromUrl -> {
                repo.fetchBookmarkObject(
                    ctx = params.ctx,
                    url = params.url
                )
            }
        }
    }

    sealed class Params {
        abstract val ctx: Id
        data class FromUrl(
            override val ctx: Id,
            val url: Url
        ) : Params()
    }
}