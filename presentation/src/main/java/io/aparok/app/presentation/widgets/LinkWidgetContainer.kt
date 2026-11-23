package io.aparok.app.presentation.widgets

import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.notifications.NotificationStateCalculator
import io.aparok.app.domain.primitives.FieldParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class LinkWidgetContainer(
    private val space: SpaceId,
    private val widget: Widget,
    private val fieldParser: FieldParser,
    private val chatPreviewContainer: ChatPreviewContainer,
    private val spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
) : WidgetContainer {
    override val view: Flow<WidgetView.Link> = run {
        val source = widget.source
        val isChatDerived = source is Widget.Source.Default &&
                source.obj.layout == ObjectType.Layout.CHAT_DERIVED

        if (isChatDerived) {
            combine(
                chatPreviewContainer.observePreviewsBySpaceId(space),
                spaceViewSubscriptionContainer.observe()
            ) { previews, spaceViews -> previews to spaceViews }
                .distinctUntilChanged()
                .map { (previews, spaceViews) ->
                    val preview = previews.find { it.chat == source.obj.id }
                    val state = preview?.state

                    // Calculate notification state for background color
                    val chatSpaceView = spaceViews
                        .firstOrNull { it.targetSpaceId == space.id }
                    val notificationState = chatSpaceView?.let { spaceView ->
                        NotificationStateCalculator.calculateChatNotificationState(
                            chatSpace = spaceView,
                            chatId = source.obj.id
                        )
                    }
                    val isMutedAndHidden = chatSpaceView?.let {
                        NotificationStateCalculator.isMutedAndHidden(it, source.obj.id)
                    } ?: false

                    WidgetView.Link(
                        id = widget.id,
                        source = widget.source,
                        icon = widget.icon,
                        name = widget.source.getPrettyName(fieldParser),
                        sectionType = widget.sectionType,
                        counter = if (preview != null && state != null) {
                            WidgetView.ChatCounter(
                                unreadMentionCount = state.unreadMentions?.counter ?: 0,
                                unreadMessageCount = state.unreadMessages?.counter ?: 0
                            )
                        } else null,
                        notificationState = notificationState,
                        isMutedAndHidden = isMutedAndHidden
                    )
                }
        } else {
            flowOf(
                WidgetView.Link(
                    id = widget.id,
                    source = widget.source,
                    icon = widget.icon,
                    name = widget.source.getPrettyName(fieldParser),
                    sectionType = widget.sectionType
                )
            )
        }
    }
}
