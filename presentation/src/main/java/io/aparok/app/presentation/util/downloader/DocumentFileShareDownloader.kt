package io.aparok.app.presentation.util.downloader

import android.content.Context
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository

class DocumentFileShareDownloader(
    private val repo: BlockRepository,
    context: Context,
    uriFileProvider: UriFileProvider,
    dispatchers: AppCoroutineDispatchers
) : MiddlewareShareDownloader(context, uriFileProvider, dispatchers) {

    override suspend fun downloadFile(objectId: Id, path: String) = repo.downloadFile(
        Command.DownloadFile(objectId = objectId, path = path)
    )
}