package io.aparok.app.domain.page.bookmark

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

/**
 * Use-case for setting up (i.e. fetching) a bookmark from url.
 */
class SetupBookmark(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetupBookmark.Params>() {

    override suspend fun run(params: Params) = try {
        repo.setupBookmark(
            command = Command.SetupBookmark(
                context = params.context,
                target = params.target,
                url = params.url
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for setting up a bookmark from [url]
     * @property context id of the context
     * @property target id of the target block (future bookmark block)
     * @property url bookmark url
     */
    data class Params(
        val context: Id,
        val target: Id,
        val url: String
    )
}