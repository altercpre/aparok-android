package io.aparok.app.feature_discussions.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.chats.ChatContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.media.PreloadFile
import io.aparok.app.domain.media.UploadFile
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import javax.inject.Inject

class DiscussionViewModelFactory @Inject constructor(
    private val params: DiscussionViewModel.Params,
    private val chatContainer: ChatContainer,
    private val members: ActiveSpaceMemberSubscriptionContainer,
    private val getAccount: GetAccount,
    private val urlBuilder: UrlBuilder,
    private val dispatchers: AppCoroutineDispatchers,
    private val addChatMessage: AddComment,
    private val deleteComment: DeleteComment,
    private val toggleCommentReaction: ToggleCommentReaction,
    private val dateProvider: DateProvider,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val fieldParser: FieldParser,
    private val subscription: StorelessSubscriptionContainer,
    private val uploadFile: UploadFile,
    private val preloadFile: PreloadFile,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DiscussionViewModel(
        vmParams = params,
        chatContainer = chatContainer,
        members = members,
        getAccount = getAccount,
        urlBuilder = urlBuilder,
        dispatchers = dispatchers,
        addChatMessage = addChatMessage,
        deleteComment = deleteComment,
        toggleCommentReaction = toggleCommentReaction,
        dateProvider = dateProvider,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser,
        subscription = subscription,
        uploadFile = uploadFile,
        preloadFile = preloadFile,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        contentResolver = context.contentResolver
    ) as T
}
