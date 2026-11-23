package io.aparok.app.domain.workspace

import io.aparok.app.core_models.Process
import kotlinx.coroutines.flow.Flow

interface EventProcessImportChannel {
    fun observe(): Flow<List<Process.Event.Import>>
}

interface EventProcessDropFilesChannel {
    fun observe(): Flow<List<Process.Event.DropFiles>>
}

interface EventProcessMigrationChannel {
    fun observe(): Flow<List<Process.Event.Migration>>
}