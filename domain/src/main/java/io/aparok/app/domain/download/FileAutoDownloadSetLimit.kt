package io.aparok.app.domain.download

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Sets the maximum size (in mebibytes) of files middleware will auto-download.
 *
 * Wraps the middleware `FileAutoDownloadSetLimit` RPC. Semantics of
 * [Params.sizeLimitMebibytes]: `0` means no upper bound, any positive value is
 * the max file size in mebibytes.
 *
 * Middleware only honors this limit when auto-download is enabled via
 * [FileSetAutoDownload]; callers should therefore skip this RPC when the feature
 * is being disabled.
 */
class FileAutoDownloadSetLimit @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<FileAutoDownloadSetLimit.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.fileAutoDownloadSetLimit(sizeLimitMebibytes = params.sizeLimitMebibytes)
    }

    data class Params(
        val sizeLimitMebibytes: Long
    )
}
