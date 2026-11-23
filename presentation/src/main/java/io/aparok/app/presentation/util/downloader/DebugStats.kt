package io.aparok.app.presentation.util.downloader

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.device.PathProvider
import java.io.File
import javax.inject.Inject

class DebugStats @Inject constructor(
    private val pathProvider: PathProvider,
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, String>(dispatchers.io) {

    override suspend fun doWork(params: Unit): String {

        val timestamp = System.currentTimeMillis()
        val path = "${pathProvider.cachePath()}/debug/stat/$timestamp/"
        val fileName = "debug_stat_$timestamp.json"
        val filePath = "$path$fileName"

        // Create the directories if they do not exist
        File(path).apply { mkdirs() }

        // Get the debug stats JSON from repository
        val jsonString = repo.debugStats()

        // Save the JSON to file
        File(filePath).writeText(jsonString)

        // Return the directory path
        return path
    }
}