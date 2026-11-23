package io.aparok.app.presentation.util.downloader

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.presentation.util.downloader.DebugSpace.Params
import java.io.File
import javax.inject.Inject

class DebugSpace @Inject constructor(
    private val repo: BlockRepository,
    private val pathProvider: PathProvider,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        // Get the space summary data from repository
        val spaceSummaryData = repo.debugSpace(space = params.spaceId)

        // Create the file path with timestamp for uniqueness
        val timestamp = System.currentTimeMillis()
        val path = "${pathProvider.cachePath()}/debug/space/${params.spaceId.id.take(16)}/"
        val fileName = "space_summary_$timestamp.json"
        val filePath = "$path$fileName"

        // Create the directories if they do not exist
        File(path).apply { mkdirs() }

        // Save the space summary data to file
        val file = File(filePath)
        file.writeText(spaceSummaryData)

        // Return the directory path (not file path) for sharing compatibility
        return path
    }

    data class Params(
        val spaceId: SpaceId
    )
}