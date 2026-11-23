package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Process
import io.aparok.app.domain.workspace.EventProcessDropFilesChannel
import io.aparok.app.domain.workspace.EventProcessImportChannel
import io.aparok.app.domain.workspace.EventProcessMigrationChannel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface EventProcessImportRemoteChannel {
    fun observe(): Flow<List<Process.Event.Import>>
}

class EventProcessImportDateChannel(
    private val channel: EventProcessImportRemoteChannel
) : EventProcessImportChannel {

    override fun observe(): Flow<List<Process.Event.Import>> {
        return channel.observe()
    }
}

interface EventProcessDropFilesRemoteChannel {
    fun observe(): Flow<List<Process.Event.DropFiles>>
}

class EventProcessDropFilesDateChannel(
    private val channel: EventProcessDropFilesRemoteChannel
) : EventProcessDropFilesChannel {

    override fun observe(): Flow<List<Process.Event.DropFiles>> {
        return channel.observe()
    }
}

interface EventProcessMigrationRemoteChannel {
    fun observe(): Flow<List<Process.Event.Migration>>
}

class EventProcessMigrationDateChannel @Inject constructor(
    private val channel: EventProcessMigrationRemoteChannel
) : EventProcessMigrationChannel {

    override fun observe(): Flow<List<Process.Event.Migration>> {
        return channel.observe()
    }
}
