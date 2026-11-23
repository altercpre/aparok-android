package io.aparok.app.core_models.history

import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectView
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.TimestampInSeconds

data class Version(
    val id: Id,
    val previousIds: List<Id>,
    val spaceMember: Id,
    val spaceMemberName: String,
    val timestamp: TimestampInSeconds,
    val groupId: Long
)

data class ShowVersionResponse(
    val payload: Payload?,
    val version: Version?,
    val traceId: Id
)

data class DiffVersionResponse(
    val historyEvents: List<Event.Command>,
    val objectView: ObjectView?
)
