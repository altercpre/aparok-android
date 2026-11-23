package io.aparok.app.domain.chats

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetRecentlyUsedChatReactions @Inject constructor(
    private val auth: AuthRepository,
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Set<String>, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Set<String>) {
        val acc = auth.getCurrentAccount()
        repo.setRecentlyUsedChatReactions(
            account = acc,
            emojis = params
        )
    }
}